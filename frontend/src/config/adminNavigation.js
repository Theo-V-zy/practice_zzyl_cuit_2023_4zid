export const prototypeNavigation = [
  {
    id: 1,
    mname: '工作台',
    path: '/Dashboard',
    sort: 1,
    subItems: [{ id: 2, mname: '工作台', path: '/Dashboard', sort: 1, subItems: [] }]
  },
  {
    id: 10,
    mname: '来访管理',
    sort: 2,
    subItems: [
      {
        id: 11,
        mname: '预约来访',
        sort: 1,
        subItems: [
          { id: 12, mname: '预约登记', path: '/Visit', sort: 1, subItems: [] },
          { id: 13, mname: '来访登记', path: '/VisitRecord', sort: 2, subItems: [] }
        ]
      }
    ]
  },
  {
    id: 20,
    mname: '入退管理',
    sort: 3,
    subItems: [
      { id: 21, mname: '入住管理', sort: 1, subItems: [{ id: 22, mname: '入住办理', path: '/Apply', sort: 1, subItems: [] }] },
      { id: 23, mname: '退住管理', sort: 2, subItems: [{ id: 24, mname: '退住办理', path: '/Checkout', sort: 1, subItems: [] }] }
    ]
  },
  {
    id: 30,
    mname: '在住管理',
    sort: 4,
    subItems: [
      { id: 31, mname: '合同管理', sort: 1, subItems: [{ id: 32, mname: '合同跟踪', path: '/Resident', sort: 1, subItems: [] }] },
      {
        id: 33,
        mname: '床位管理',
        sort: 2,
        subItems: [
          { id: 34, mname: '床位房型', path: '/Bed', sort: 1, subItems: [] },
          { id: 35, mname: '智能床位', path: '/SmartBed', sort: 2, subItems: [] },
          { id: 36, mname: '房型设置', path: '/RoomType', sort: 3, subItems: [] }
        ]
      },
      { id: 37, mname: '请假管理', sort: 3, subItems: [{ id: 38, mname: '请假管理', path: '/Leave', sort: 1, subItems: [] }] }
    ]
  },
  {
    id: 40,
    mname: '服务管理',
    sort: 5,
    subItems: [
      {
        id: 41,
        mname: '护理计划',
        sort: 1,
        subItems: [
          { id: 42, mname: '护理等级', path: '/NursingLevel', sort: 1, subItems: [] },
          { id: 43, mname: '护理计划', path: '/NursingPlain', sort: 2, subItems: [] },
          { id: 44, mname: '护理项目', path: '/NursingItem', sort: 3, subItems: [] }
        ]
      },
      {
        id: 45,
        mname: '护理任务',
        sort: 2,
        subItems: [
          { id: 46, mname: '负责老人', path: '/ElderAssignment', sort: 1, subItems: [] },
          { id: 47, mname: '任务安排', path: '/NursingTask', sort: 2, subItems: [] }
        ]
      }
    ]
  },
  {
    id: 50,
    mname: '订单管理',
    sort: 6,
    subItems: [
      { id: 51, mname: '订单管理', path: '/Order', sort: 1, subItems: [] },
      { id: 52, mname: '退款管理', path: '/Refund', sort: 2, subItems: [] }
    ]
  },
  {
    id: 60,
    mname: '财务管理',
    sort: 7,
    subItems: [
      { id: 61, mname: '账单管理', sort: 1, subItems: [{ id: 62, mname: '入账列表', path: '/Bill', sort: 1, subItems: [] }, { id: 63, mname: '欠费老人', path: '/Arrears', sort: 2, subItems: [] }] },
      { id: 64, mname: '预存管理', sort: 2, subItems: [{ id: 65, mname: '预缴款充值', path: '/Prepay', sort: 1, subItems: [] }, { id: 66, mname: '余额查询', path: '/Balance', sort: 2, subItems: [] }] }
    ]
  },
  {
    id: 70,
    mname: '客户管理',
    sort: 8,
    subItems: [{ id: 71, mname: '客户信息', path: '/Customer', sort: 1, subItems: [] }]
  },
  {
    id: 80,
    mname: '权限管理',
    sort: 9,
    subItems: [
      { id: 81, mname: '用户管理', sort: 1, subItems: [{ id: 82, mname: '用户信息', path: '/UserManage', sort: 1, subItems: [] }] },
      {
        id: 83,
        mname: '权限配置',
        sort: 2,
        subItems: [
          { id: 84, mname: '角色管理-菜单权限', path: '/RoleManage', sort: 1, subItems: [] },
          { id: 85, mname: '角色管理-数据权限', path: '/RoleDataScope', sort: 2, subItems: [] },
          { id: 86, mname: '菜单管理', path: '/MenuManage', sort: 3, subItems: [] },
          { id: 87, mname: '部门管理', path: '/Department', sort: 4, subItems: [] },
          { id: 88, mname: '职位管理', path: '/Post', sort: 5, subItems: [] }
        ]
      }
    ]
  },
  {
    id: 90,
    mname: '协同工作',
    sort: 10,
    subItems: [
      { id: 91, mname: '我的待办', path: '/Todo', sort: 1, subItems: [] },
      { id: 92, mname: '我的申请', path: '/Application', sort: 2, subItems: [] }
    ]
  },
  {
    id: 100,
    mname: '智能监测',
    sort: 11,
    subItems: [
      { id: 101, mname: '设备管理', sort: 1, subItems: [{ id: 102, mname: '设备管理', path: '/Device', sort: 1, subItems: [] }] },
      { id: 103, mname: '报警管理', sort: 2, subItems: [{ id: 104, mname: '报警数据', path: '/Alert', sort: 1, subItems: [] }, { id: 105, mname: '报警规则', path: '/AlertRule', sort: 2, subItems: [] }] }
    ]
  },
  {
    id: 200,
    mname: '个人中心',
    topVisible: false,
    subItems: [
      { id: 201, mname: '个人信息', path: '/UserInfo', sort: 1, subItems: [] },
      { id: 202, mname: '修改密码', path: '/ModifyPwd', sort: 2, subItems: [] }
    ]
  },
  {
    id: 110,
    mname: 'AI助手',
    sort: 12,
    subItems: [{ id: 111, mname: '智能问答', path: '/AIChat', sort: 1, subItems: [] }]
  },
  {
    id: 210,
    mname: '消息中心',
    topVisible: false,
    subItems: [{ id: 211, mname: '消息通知', path: '/Messages', sort: 1, subItems: [] }]
  }
]
