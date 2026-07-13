package com.soft.controller;

import com.soft.pojo.FamilyUser;
import com.soft.service.FamilyTokenService;
import com.soft.service.FamilyUserService;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/family")
public class FamilyController {

    private final FamilyUserService familyUserService;
    private final FamilyTokenService tokenService;
    private final JdbcTemplate jdbcTemplate;

    public FamilyController(FamilyUserService familyUserService,
                            FamilyTokenService tokenService,
                            JdbcTemplate jdbcTemplate) {
        this.familyUserService = familyUserService;
        this.tokenService = tokenService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        Map<String, Object> result = familyUserService.login(body.get("account"), body.get("password"));
        if (Objects.equals(result.get("code"), 200)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> userInfo = (Map<String, Object>) result.get("data");
            userInfo.put("token", tokenService.createToken((Integer) userInfo.get("id")));
        }
        return result;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        return success("退出成功");
    }

    @GetMapping("/profile")
    public Map<String, Object> profile(@RequestHeader(value = "X-Family-Token", required = false) String token) {
        FamilyUser user = familyUserService.getById(requireFamilyId(token));
        if (user == null || Objects.equals(user.getStatus(), 0)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "账号不存在或已禁用");
        }
        return data(userView(user));
    }

    @PutMapping("/profile")
    public Map<String, Object> updateProfile(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                             @RequestBody Map<String, Object> body) {
        Integer familyId = requireFamilyId(token);
        jdbcTemplate.update("UPDATE t_family_user SET name = ?, phone = ?, avatar = ? WHERE id = ?",
                text(body.get("name")), text(body.get("phone")), nullableText(body.get("avatar")), familyId);
        return success("资料保存成功");
    }

    @GetMapping({"/mine", "/home"})
    public Map<String, Object> home(@RequestHeader(value = "X-Family-Token", required = false) String token) {
        Integer familyId = requireFamilyId(token);
        FamilyUser user = familyUserService.getById(familyId);
        Map<String, Object> result = success(null);
        result.putAll(userView(user));
        result.put("elders", elders(familyId));
        result.put("nursingHome", Map.of(
                "name", "中州养老",
                "introduction", "以专业照护、健康管理和温暖陪伴，为长者提供安心舒适的养老生活。",
                "address", "河南省郑州市中州大道100号",
                "phone", "0371-12345678"));
        result.put("roomTypes", roomTypes());
        return result;
    }

    @GetMapping("/elders")
    public Map<String, Object> elderList(@RequestHeader(value = "X-Family-Token", required = false) String token) {
        return data(elders(requireFamilyId(token)));
    }

    @GetMapping("/elders/{elderId}")
    public Map<String, Object> elderDetail(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                           @PathVariable Integer elderId) {
        Integer familyId = requireFamilyId(token);
        ensureElderOwned(familyId, elderId);
        Map<String, Object> elder = jdbcTemplate.queryForMap(
                "SELECT e.id, e.elder_no AS elderNo, e.name, e.gender, e.birthday, e.phone, e.avatar, " +
                        "e.status, e.health_status AS healthStatus, e.ability_level AS abilityLevel, " +
                        "fe.relation, b.building, b.floor, b.room_no AS roomNo, b.bed_no AS bedNo " +
                        "FROM t_elder e JOIN t_family_elder fe ON fe.elder_id=e.id " +
                        "LEFT JOIN t_bed b ON b.elder_id=e.id " +
                        "WHERE fe.family_id=? AND fe.elder_id=? AND fe.status=1", familyId, elderId);
        elder.put("health", healthCards());
        return data(elder);
    }

    @PostMapping("/elders/bind")
    public Map<String, Object> bindElder(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                         @RequestBody Map<String, Object> body) {
        Integer familyId = requireFamilyId(token);
        String elderNo = text(body.get("elderNo"));
        String name = text(body.get("name"));
        String relation = text(body.get("relation"));
        List<Map<String, Object>> matches = jdbcTemplate.queryForList(
                "SELECT id FROM t_elder WHERE elder_no=? AND name=?", elderNo, name);
        if (matches.isEmpty()) {
            return failure("未找到匹配的老人，请核对编号和姓名");
        }
        Integer elderId = ((Number) matches.get(0).get("id")).intValue();
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM t_family_elder WHERE family_id=? AND elder_id=?", Integer.class, familyId, elderId);
        if (count != null && count > 0) {
            jdbcTemplate.update("UPDATE t_family_elder SET relation=?, status=1 WHERE family_id=? AND elder_id=?",
                    relation, familyId, elderId);
        } else {
            jdbcTemplate.update("INSERT INTO t_family_elder(family_id, elder_id, relation, is_default, status) VALUES(?,?,?,?,1)",
                    familyId, elderId, relation, elders(familyId).isEmpty() ? 1 : 0);
        }
        return success("绑定成功");
    }

    @DeleteMapping("/elders/{elderId}")
    public Map<String, Object> unbindElder(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                           @PathVariable Integer elderId) {
        Integer familyId = requireFamilyId(token);
        int changed = jdbcTemplate.update(
                "UPDATE t_family_elder SET status=0, is_default=0 WHERE family_id=? AND elder_id=? AND status=1",
                familyId, elderId);
        return changed > 0 ? success("解绑成功") : failure("绑定关系不存在");
    }

    @GetMapping("/services")
    public Map<String, Object> services(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                        @RequestParam(required = false, defaultValue = "") String keyword) {
        requireFamilyId(token);
        String sql = "SELECT id, name, price, unit, description, type FROM (" +
                "SELECT id, itemname AS name, price, unit, description, 'item' AS type, CAST(sort AS UNSIGNED) AS ord " +
                "FROM t_nursimg_item WHERE price > 0 AND (islock IS NULL OR islock IN ('启用','0','1')) " +
                "AND (?='' OR itemname LIKE CONCAT('%',?,'%')) " +
                "UNION ALL " +
                "SELECT -p.id AS id, p.plainname AS name, " +
                "COALESCE((SELECT SUM(CASE WHEN ni.unit='周' THEN ni.price*pi.hlpc/7 " +
                "WHEN ni.unit='月' THEN ni.price*pi.hlpc/30 ELSE ni.price*pi.hlpc END) " +
                "FROM t_plain_item pi JOIN t_nursimg_item ni ON pi.item_id=ni.id " +
                "WHERE pi.plain_id=p.id AND (ni.islock IS NULL OR ni.islock='启用')), 0) AS price, " +
                "'日' AS unit, " +
                "CONCAT('已选', COALESCE((SELECT COUNT(*) FROM t_plain_item pi WHERE pi.plain_id=p.id), 0), '项 | ', " +
                "COALESCE((SELECT GROUP_CONCAT(pi.itemname SEPARATOR '、') FROM t_plain_item pi WHERE pi.plain_id=p.id), '')) AS description, " +
                "'plan' AS type, 999 AS ord " +
                "FROM t_nursing_plain p WHERE p.islock='启动' AND EXISTS " +
                "(SELECT 1 FROM t_plain_item pi WHERE pi.plain_id=p.id) " +
                "AND (?='' OR p.plainname LIKE CONCAT('%',?,'%')) " +
                ") t WHERE price > 0 ORDER BY ord, id";
        return data(jdbcTemplate.queryForList(sql, keyword, keyword, keyword, keyword));
    }

    @GetMapping("/services/{serviceId}")
    public Map<String, Object> serviceDetail(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                             @PathVariable Integer serviceId) {
        requireFamilyId(token);
        // 负数ID表示护理计划
        if (serviceId < 0) {
            int planId = -serviceId;
            List<Map<String, Object>> plans = jdbcTemplate.queryForList(
                    "SELECT -p.id AS id, p.plainname AS name, " +
                    "COALESCE((SELECT SUM(CASE WHEN ni.unit='周' THEN ni.price*pi.hlpc/7 " +
                    "WHEN ni.unit='月' THEN ni.price*pi.hlpc/30 ELSE ni.price*pi.hlpc END) " +
                    "FROM t_plain_item pi JOIN t_nursimg_item ni ON pi.item_id=ni.id " +
                    "WHERE pi.plain_id=p.id AND (ni.islock IS NULL OR ni.islock='启用')), 0) AS price, " +
                    "'日' AS unit, p.level_name AS levelName, " +
                    "COALESCE((SELECT GROUP_CONCAT(pi.itemname SEPARATOR '、') FROM t_plain_item pi " +
                    "WHERE pi.plain_id=p.id), '') AS description, 'plan' AS type " +
                    "FROM t_nursing_plain p WHERE p.id=? AND p.islock='启动'",
                    planId);
            if (!plans.isEmpty()) {
                Map<String, Object> plan = plans.get(0);
                plan.put("items", jdbcTemplate.queryForList(
                        "SELECT pi.id, pi.item_id AS itemId, pi.itemname, pi.hlzq AS unit, pi.hlpc, ni.price, " +
                        "ni.description FROM t_plain_item pi " +
                        "JOIN t_nursimg_item ni ON pi.item_id=ni.id WHERE pi.plain_id=?",
                        planId));
                return data(plan);
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "套餐不存在");
        }
        // 正数ID为护理项目
        List<Map<String, Object>> items = jdbcTemplate.queryForList(
                "SELECT id, itemname AS name, price, unit, description, 'item' AS type FROM t_nursimg_item WHERE id=?",
                serviceId);
        if (!items.isEmpty()) {
            return data(items.get(0));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "服务不存在");
    }

    @PostMapping("/orders")
    public Map<String, Object> createOrder(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                           @RequestBody Map<String, Object> body) {
        Integer familyId = requireFamilyId(token);
        Integer elderId = integer(body.get("elderId"));
        Integer serviceId = integer(body.get("serviceId"));
        ensureElderOwned(familyId, elderId);
        // 套餐下单：合并为一个订单
        if (serviceId < 0) {
            int planId = -serviceId;
            List<Map<String, Object>> items = jdbcTemplate.queryForList(
                    "SELECT pi.itemname, ni.price, ni.unit FROM t_plain_item pi " +
                    "JOIN t_nursimg_item ni ON pi.item_id=ni.id WHERE pi.plain_id=?", planId);
            if (items.isEmpty()) {
                return failure("套餐内没有可用项目");
            }
            // 计算日总价
            BigDecimal total = BigDecimal.ZERO;
            StringBuilder itemList = new StringBuilder();
            for (Map<String, Object> item : items) {
                BigDecimal price = (BigDecimal) item.get("price");
                total = total.add(price);
                if (itemList.length() > 0) itemList.append("、");
                itemList.append(item.get("itemname"));
            }
            // 查计划名称
            String planName = jdbcTemplate.queryForObject(
                    "SELECT plainname FROM t_nursing_plain WHERE id=?", String.class, planId);
            String orderNo = number("DD");
            jdbcTemplate.update("INSERT INTO t_order(order_no,family_id,elder_id,service_item_id,service_name," +
                    "quantity,total_amount,pay_amount,pay_status,order_status,service_time,remark) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)",
                    orderNo, familyId, elderId, 0, planName, 1,
                    total, BigDecimal.ZERO, "UNPAID", "CREATED",
                    nullableText(body.get("serviceTime")), "套餐包含：" + itemList.toString());
            Integer orderId = jdbcTemplate.queryForObject(
                    "SELECT id FROM t_order WHERE order_no=?", Integer.class, orderNo);
            return data(Map.of("id", orderId, "orderNo", orderNo, "planName", planName, "itemCount", items.size()));
        }
        // 单项下单
        Map<String, Object> service = jdbcTemplate.queryForMap(
                "SELECT itemname, price FROM t_nursimg_item WHERE id=?", serviceId);
        int quantity = Math.max(1, integer(body.getOrDefault("quantity", 1)));
        BigDecimal price = (BigDecimal) service.get("price");
        BigDecimal total = price.multiply(BigDecimal.valueOf(quantity));
        String orderNo = number("DD");
        jdbcTemplate.update("INSERT INTO t_order(order_no,family_id,elder_id,service_item_id,service_name,quantity," +
                        "total_amount,pay_amount,pay_status,order_status,service_time,remark) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)",
                orderNo, familyId, elderId, serviceId, service.get("itemname"), quantity, total, BigDecimal.ZERO,
                "UNPAID", "CREATED", nullableText(body.get("serviceTime")), nullableText(body.get("remark")));
        Integer orderId = jdbcTemplate.queryForObject("SELECT id FROM t_order WHERE order_no=?", Integer.class, orderNo);
        return data(Map.of("id", orderId, "orderNo", orderNo));
    }

    @PostMapping("/orders/page")
    public Map<String, Object> ordersPage(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                          @RequestBody(required = false) Map<String, Object> params) {
        Integer familyId = requireFamilyId(token);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT o.id,o.order_no AS orderNo,o.service_item_id AS serviceId,o.service_name AS serviceName," +
                        "o.total_amount AS totalAmount,o.pay_amount AS payAmount,o.pay_status AS payStatus," +
                        "o.order_status AS orderStatus,o.service_time AS serviceTime,o.create_time AS createTime," +
                        "e.name AS elderName FROM t_order o LEFT JOIN t_elder e ON o.elder_id=e.id " +
                        "WHERE o.family_id=? OR o.elder_id IN (SELECT elder_id FROM t_family_elder WHERE family_id=? AND status=1) " +
                        "ORDER BY o.create_time DESC", familyId, familyId);
        return page(list);
    }

    @GetMapping("/orders/{id}")
    public Map<String, Object> orderDetail(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                           @PathVariable Integer id) {
        Integer familyId = requireFamilyId(token);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT o.id,o.order_no AS orderNo,o.service_name AS serviceName,o.total_amount AS totalAmount," +
                        "o.pay_amount AS payAmount,o.pay_status AS payStatus,o.order_status AS orderStatus,o.quantity," +
                        "o.create_time AS createTime,o.service_time AS serviceTime,o.remark,o.refund_reason AS refundReason," +
                        "o.refund_amount AS refundAmount,e.name AS elderName FROM t_order o " +
                        "LEFT JOIN t_elder e ON o.elder_id=e.id " +
                        "WHERE o.id=? AND (o.family_id=? OR o.elder_id IN " +
                        "(SELECT elder_id FROM t_family_elder WHERE family_id=? AND status=1))", id, familyId, familyId);
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "订单不存在");
        }
        return data(list.get(0));
    }

    @PostMapping("/orders/{id}/pay")
    public Map<String, Object> payOrder(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                        @PathVariable Integer id) {
        Integer familyId = requireFamilyId(token);
        int changed = jdbcTemplate.update("UPDATE t_order SET pay_status='PAID',order_status='PAID',pay_amount=total_amount " +
                "WHERE id=? AND family_id=? AND order_status='CREATED'", id, familyId);
        return changed > 0 ? success("支付成功") : failure("订单状态已变化");
    }

    @PostMapping("/orders/{id}/cancel")
    public Map<String, Object> cancelOrder(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                           @PathVariable Integer id,
                                           @RequestBody(required = false) Map<String, Object> body) {
        Integer familyId = requireFamilyId(token);
        int changed = jdbcTemplate.update("UPDATE t_order SET order_status='CANCELED',remark=? " +
                        "WHERE id=? AND family_id=? AND order_status='CREATED'",
                body == null ? "用户取消" : nullableText(body.get("reason")), id, familyId);
        return changed > 0 ? success("订单已取消") : failure("当前订单不可取消");
    }

    @PostMapping("/orders/{id}/refund")
    public Map<String, Object> refundOrder(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                           @PathVariable Integer id,
                                           @RequestBody Map<String, Object> body) {
        Integer familyId = requireFamilyId(token);
        int changed = jdbcTemplate.update("UPDATE t_order SET order_status='REFUNDED',refund_amount=pay_amount,refund_reason=? " +
                        "WHERE id=? AND family_id=? AND order_status IN ('PAID','SERVING')",
                nullableText(body.get("reason")), id, familyId);
        return changed > 0 ? success("退款申请已提交") : failure("当前订单不可退款");
    }

    @DeleteMapping("/orders/{id}")
    public Map<String, Object> deleteOrder(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                           @PathVariable Integer id) {
        Integer familyId = requireFamilyId(token);
        int changed = jdbcTemplate.update("DELETE FROM t_order WHERE id=? AND family_id=? " +
                "AND order_status IN ('FINISHED','DONE','CANCELED','REFUNDED')", id, familyId);
        return changed > 0 ? success("订单已删除") : failure("当前订单不可删除");
    }

    @GetMapping("/appointments")
    public Map<String, Object> appointments(@RequestHeader(value = "X-Family-Token", required = false) String token) {
        Integer familyId = requireFamilyId(token);
        FamilyUser user = familyUserService.getById(familyId);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT v.id,v.visit_no AS visitNo,v.visit_type AS visitType,v.visitor_name AS visitorName," +
                        "v.visitor_phone AS visitorPhone,v.appointment_time AS appointmentTime,v.status," +
                        "v.remark,COALESCE(e.name,v.elder_name) AS elderName FROM t_visit v LEFT JOIN t_elder e ON v.elder_id=e.id " +
                        "WHERE v.visitor_phone=? OR v.elder_id IN " +
                        "(SELECT elder_id FROM t_family_elder WHERE family_id=? AND status=1) ORDER BY v.appointment_time DESC",
                user.getPhone(), familyId);
        return page(list);
    }

    @PostMapping("/appointments")
    public Map<String, Object> createAppointment(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                                 @RequestBody Map<String, Object> body) {
        Integer familyId = requireFamilyId(token);
        FamilyUser user = familyUserService.getById(familyId);
        String type = text(body.getOrDefault("visitType", "VISIT"));
        Integer elderId = integerOrNull(body.get("elderId"));
        String elderName = null;
        if (Objects.equals(type, "VISIT")) {
            if (elderId == null) {
                return failure("请选择探访家人");
            }
            ensureElderOwned(familyId, elderId);
            elderName = jdbcTemplate.queryForObject("SELECT name FROM t_elder WHERE id=?", String.class, elderId);
        }
        jdbcTemplate.update("INSERT INTO t_visit(visit_no,visit_stage,visitor_name,visitor_phone,visit_type," +
                        "elder_id,elder_name,appointment_time,status,remark) VALUES(?,?,?,?,?,?,?,?,?,?)",
                number("YY"), "RESERVATION", text(body.getOrDefault("visitorName", user.getName())),
                text(body.getOrDefault("visitorPhone", user.getPhone())), type, elderId, elderName,
                text(body.get("appointmentTime")), "PENDING", nullableText(body.get("remark")));
        return success("预约提交成功");
    }

    @PostMapping("/appointments/{id}/cancel")
    public Map<String, Object> cancelAppointment(@RequestHeader(value = "X-Family-Token", required = false) String token,
                                                 @PathVariable Integer id) {
        Integer familyId = requireFamilyId(token);
        FamilyUser user = familyUserService.getById(familyId);
        int changed = jdbcTemplate.update("UPDATE t_visit SET status='CANCELED' WHERE id=? AND status='PENDING' " +
                        "AND (visitor_phone=? OR elder_id IN (SELECT elder_id FROM t_family_elder WHERE family_id=? AND status=1))",
                id, user.getPhone(), familyId);
        return changed > 0 ? success("预约已取消") : failure("当前预约不可取消");
    }

    @GetMapping("/contracts")
    public Map<String, Object> contracts(@RequestHeader(value = "X-Family-Token", required = false) String token) {
        Integer familyId = requireFamilyId(token);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT c.id,c.contract_no AS contractNo,c.start_date AS startDate,c.end_date AS endDate," +
                        "c.monthly_fee AS monthlyFee,c.status,c.create_time AS signDate,e.name AS elderName " +
                        "FROM t_contract c LEFT JOIN t_elder e ON c.elder_id=e.id " +
                        "WHERE c.family_id=? OR c.elder_id IN (SELECT elder_id FROM t_family_elder WHERE family_id=? AND status=1) " +
                        "ORDER BY c.create_time DESC", familyId, familyId);
        return page(list);
    }

    @GetMapping("/bills")
    public Map<String, Object> bills(@RequestHeader(value = "X-Family-Token", required = false) String token) {
        Integer familyId = requireFamilyId(token);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT b.id,b.bill_no AS billNo,b.bill_type AS billType,b.fee_name AS feeName,b.bill_month AS billMonth," +
                        "b.total_amount AS totalAmount,b.paid_amount AS paidAmount,b.status,e.name AS elderName " +
                        "FROM t_bill b LEFT JOIN t_elder e ON b.elder_id=e.id " +
                        "WHERE b.family_id=? OR b.elder_id IN (SELECT elder_id FROM t_family_elder WHERE family_id=? AND status=1) " +
                        "ORDER BY b.create_time DESC", familyId, familyId);
        return page(list);
    }

    private Integer requireFamilyId(String token) {
        Integer familyId = tokenService.parseFamilyId(token);
        if (familyId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "请先登录");
        }
        return familyId;
    }

    private void ensureElderOwned(Integer familyId, Integer elderId) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_family_elder " +
                "WHERE family_id=? AND elder_id=? AND status=1", Integer.class, familyId, elderId);
        if (count == null || count == 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "无权操作该老人数据");
        }
    }

    private List<Map<String, Object>> elders(Integer familyId) {
        return jdbcTemplate.queryForList("SELECT e.id,e.name,e.elder_no AS elderNo,e.gender,e.avatar,e.status," +
                "e.health_status AS healthStatus,e.ability_level AS abilityLevel,fe.relation,fe.is_default AS isDefault," +
                "b.building,b.floor,b.room_no AS roomNo,b.bed_no AS bedNo FROM t_family_elder fe " +
                "JOIN t_elder e ON fe.elder_id=e.id LEFT JOIN t_bed b ON b.elder_id=e.id " +
                "WHERE fe.family_id=? AND fe.status=1 ORDER BY fe.is_default DESC,fe.create_time", familyId);
    }

    private List<Map<String, Object>> roomTypes() {
        List<Map<String, Object>> rooms = jdbcTemplate.queryForList("SELECT room_type AS name,MIN(bed_price) AS price," +
                "COUNT(*) AS bedCount FROM t_bed GROUP BY room_type ORDER BY MIN(id)");
        for (Map<String, Object> room : rooms) {
            room.put("description", "房间整洁明亮，配备适老设施与全天候照护服务。");
        }
        return rooms;
    }

    private List<Map<String, Object>> healthCards() {
        String time = LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
        return List.of(
                Map.of("name", "血压", "value", "128/82mmHg", "status", "正常", "time", time),
                Map.of("name", "心率", "value", "78次/分", "status", "正常", "time", time),
                Map.of("name", "血氧", "value", "98%", "status", "正常", "time", time),
                Map.of("name", "体温", "value", "36.5℃", "status", "正常", "time", time));
    }

    private Map<String, Object> userView(FamilyUser user) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", user.getId());
        data.put("account", user.getAccount());
        data.put("name", user.getName());
        data.put("phone", user.getPhone());
        data.put("avatar", user.getAvatar());
        return data;
    }

    private Map<String, Object> data(Object value) {
        Map<String, Object> result = success(null);
        result.put("data", value);
        return result;
    }

    private Map<String, Object> page(List<Map<String, Object>> list) {
        Map<String, Object> result = data(list);
        result.put("total", list.size());
        return result;
    }

    private Map<String, Object> success(String message) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 200);
        if (message != null) {
            result.put("msg", message);
        }
        return result;
    }

    private Map<String, Object> failure(String message) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 400);
        result.put("msg", message);
        return result;
    }

    private String number(String prefix) {
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    private String text(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private String nullableText(Object value) {
        String result = text(value);
        return result.isEmpty() ? null : result;
    }

    private Integer integer(Object value) {
        return Integer.valueOf(String.valueOf(value));
    }

    private Integer integerOrNull(Object value) {
        return value == null || String.valueOf(value).isBlank() ? null : integer(value);
    }
}
