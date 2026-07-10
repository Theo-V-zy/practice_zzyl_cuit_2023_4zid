# Design QA

- Source visual truth: `docs/design-evidence/source-workbench-desktop.png`, `docs/design-evidence/source-visit-desktop.png`
- Implementation screenshot: `docs/design-evidence/implementation-workbench-1280.png`
- Combined comparison: `docs/design-evidence/workbench-comparison.png`
- Viewport: 1280 x 720 browser viewport; source application frame normalized to the same width
- State: management shell, workbench selected; module switching additionally checked with visit management selected

## Full-view comparison evidence

The implementation now follows the prototype shell: a 64px horizontal primary-module header, a 232px white contextual sidebar, a light gray workspace, a white content panel, the Zhongzhou Elderly Care logo, and the administrator entry on the right. The whole page no longer requires horizontal scrolling; only the primary-module strip can scroll when the viewport is narrow.

## Focused region comparison evidence

Header and sidebar were checked separately because these are the shared surfaces every teammate will reuse. The source top-module order, sidebar hierarchy, logo treatment, typography scale, neutral palette, and selected menu state are represented in the implementation. Visit Management switches the sidebar to Reservation Visit, Reservation Registration, and Visit Registration without replacing the shell.

## Findings

- No actionable P0/P1/P2 mismatch remains in the shared navigation shell.
- Dashboard business cards and charts are intentionally outside this QA target; the current content is a route placeholder for the responsible developer.

## Required fidelity surfaces

- Fonts and typography: PingFang SC system stack, 14px navigation text, calligraphic Chinese logo fallback, and compact English subtitle match the source hierarchy.
- Spacing and layout rhythm: 64px header, 232px sidebar, 24px workspace padding, and 40px sidebar rows match the captured shell proportions.
- Colors and visual tokens: white sidebar, light gray header/workspace, dark neutral text, and blue selected state match the prototype family.
- Image quality and asset fidelity: the original prototype logo mark and administrator avatar were copied locally; no hotlinks or handmade SVG replacements are used.
- Copy and content: all 11 top-level module names and the prototype sidebar labels are preserved.

## Comparison history

- Earlier finding: the first implementation used a dark full-height sidebar containing every module and a utility-only top bar. This was a P1 structural mismatch.
- Fix: moved the 11 primary modules to the top, limited the sidebar to the active module, restored the source logo and neutral palette, and retained responsive internal scrolling.
- Post-fix evidence: `docs/design-evidence/implementation-workbench-1280.png`; module-switch DOM verification and browser console reported no errors.

## Follow-up polish

- Replace each placeholder with its assigned business page while keeping `AdminLayout.vue` unchanged.

final result: passed
