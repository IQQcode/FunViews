# FunViews mgit 多仓 Demo（GitSpace 推送/拉取测试环境）

壳工程 `FunViews` + 6 个独立组件仓，用于验证 GitSpace 的批量 Fetch / Pull / Push、逐仓提交、
跨仓时间线、分支与 Tag 操作。分支：`mgit_multi_components`（拆分前的原始分支保留为 tag
`backup/pre-mgit-split`）。

## 1. 结构

```text
~/Documents/Code/FunViews/                 壳工程（app + linkage-recyclerview 仍由壳仓纳管）
├── app/                                   ← 壳仓
├── linkage-recyclerview/                  ← 壳仓
├── lib_common/            独立 .git  ──→  FunViews-remotes/lib_common.git
├── module_core/           独立 .git  ──→  FunViews-remotes/module_core.git
├── module_layout/         独立 .git  ──→  FunViews-remotes/module_layout.git
├── module_components/     独立 .git  ──→  FunViews-remotes/module_components.git
├── module_animations/     独立 .git  ──→  FunViews-remotes/module_animations.git
├── module_frameworks/     独立 .git  ──→  FunViews-remotes/module_frameworks.git
├── docs/mgit-manifest.txt                 导入清单（每行一个仓库路径）
└── scripts/
    ├── report-states.sh                   状态对账（--check 与矩阵比对）
    ├── seed-scenarios.sh                  场景重铺（幂等）
    ├── smoke-push-pull.sh                 冒烟跑通全矩阵并断言结果
    ├── use-github-remote.sh               壳工程上游切 GitHub/本地镜像
    ├── reset-demo.sh                      卸载整套 Demo
    └── seeds/<仓库>/<文件>                场景 fixture 源码

~/Documents/Code/FunViews-remotes/          全部本地 bare 远程仓（file:// 等价远程）
```

组件目录在壳工程 `.gitignore` 中以 `/lib_common/` 等形式显式排除，壳仓不再纳管其内容；
`settings.gradle` 未做改动，Gradle 聚合方式与拆分前一致。

各子仓的历史由壳仓历史经 `git filter-branch --subdirectory-filter --prune-empty` 拆出，
因此每个组件仓带有真实的提交日期、作者与逐模块提交数（12/5/9/12/6/3 条），
跨仓统一时间线才有可观测的数据。

## 2. 远程映射

| 仓库 | remote | 说明 |
| --- | --- | --- |
| FunViews | `origin` | `git@github.com:IQQcode/FunViews.git`（SSH）。已验证 fetch 与 push 写路径可用，且全程非交互——与 GitSpace 的 `GIT_TERMINAL_PROMPT=0` 环境一致，所以 UI 侧能真推 GitHub |
| FunViews | `local`（当前 upstream） | `FunViews-remotes/FunViews.git`，离线可用的镜像；矩阵的 ahead/behind 只以它为基准 |
| 6 个组件仓 | `origin` | 各自指向 `FunViews-remotes/<模块>.git` |

GitSpace 的推送执行的是裸 `git push`（见 `GitRepository+Write.swift`），落点由**当前分支的上游**
决定，不是由 remote 名字决定。所以要让 UI 侧真推到 GitHub：

```bash
bash scripts/use-github-remote.sh on      # 壳工程上游切到 origin/mgit_multi_components（GitHub）
bash scripts/use-github-remote.sh status  # 查看当前上游、目标 URL、ahead/behind
bash scripts/use-github-remote.sh off     # 测完切回 local 镜像（矩阵基准）
```

已在 GitHub 侧实测写路径：临时分支创建与删除均成功，且 `GIT_TERMINAL_PROMPT=0` 下全程非交互
（引擎只透传 `SSH_AUTH_SOCK`/PATH 等环境并关闭交互提示，SSH 免提示可用）。GitHub 模式下
`report-states.sh --check` 会对 FunViews 报 DRIFT——矩阵的 ahead 基准是 `local` 镜像，属预期，
切回 `off` 即恢复一致。6 个组件仓的 `origin` 仍是本地 bare，不受这个开关影响。

## 3. 场景矩阵

| 仓库 | 分支 | upstream | ahead | behind | 工作区 | Tag | 覆盖能力 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| FunViews | `mgit_multi_components` | `local/mgit_multi_components` | 2 | 0 | 干净 | `backup/pre-mgit-split`（未推送） | 批量 Push、未推送提交计数、双远程（GitHub `origin` 已有该分支；重铺产出相同 hash，不会被甩成分叉）、跨仓时间线 |
| lib_common | `main` | `origin/main` | 0 | 0 | 干净 | — | 同步基线：状态徽章应为「已同步」，批量操作时被跳过 |
| module_core | `main` | `origin/main` | 2 | 0 | 干净 | — | 待推送：批量 Push 自动选中、Push 后计数归零 |
| module_layout | `main` | `origin/main` | 0 | 2 | 干净 | — | 落后远程：全部 Fetch + 批量 Pull（fast-forward），远程提交作者为 zhang-san |
| module_components | `main` | `origin/main` | 1 | 1 | 干净 | — | 分叉：Pull 走 merge/rebase 分支逻辑，两侧改动不同文件（可干净合并） |
| module_animations | `main` | `origin/main` | 0 | 0 | 脏：1 已暂存(新增后又改动) + 2 未暂存 + 1 未跟踪 | — | 逐仓提交、暂存/未暂存/未跟踪三态徽章、提交后未推送计数 |
| module_frameworks | `feature/story-2001-renderer`（无 upstream） | — | — | — | 干净 | `v1.2.0-story-2001`（未推送） | 新分支首推、分支列表与 `release/1.1.0` 待删分支、Tag 推送 |

Story-2001「购物车结算链路」是贯穿 lib_common → module_core → module_components →
module_frameworks → 壳工程的一条需求线，提交信息里都带 `Story-2001`，
可直接在跨仓时间线里按关键字过滤验证聚合是否正确。

## 4. 导入 GitSpace

两种方式：

1. **导入 mgit 清单**：粘贴 `docs/mgit-manifest.txt` 内容（7 行路径，`~` 开头会被展开）。
2. **扫描父目录**：对 `~/Documents/Code` 扫描（深度 3）。注意 `FunViews-remotes/` 下的 7 个
   bare 仓也会被识别为仓库——它们没有工作区，正好可用来验证异常态与批量操作对 bare 仓的行为。
   不想被扫到就把 `FunViews-remotes` 挪出 `~/Documents/Code`，并同步改各仓 `origin` 地址。

## 5. 对账

```bash
bash scripts/report-states.sh          # 打印 7 个仓库的分支/领先落后/三态/Tag
bash scripts/report-states.sh --check  # 与本文件矩阵比对，漂移退出码 1
```

自动化测试的推荐用法：每轮用例开始前跑一次 `--check` 确认初始态；
用例结束后用第 6 节命令回到初始态。

### 5.1 冒烟跑通（CLI 等价基线）

```bash
bash scripts/smoke-push-pull.sh            # 跑完自动重铺回初始态
bash scripts/smoke-push-pull.sh --keep     # 保留消耗后的状态，便于手工接着验证
bash scripts/smoke-push-pull.sh --force    # 跳过「初始态必须符合矩阵」的前置断言
```

脚本按 GitSpace 的操作语义逐条执行并断言（GitSpace 同样是 shell out 到系统 git CLI，
所以这张结果表就是 UI 侧的等价基准），任一条 FAIL 退出码非 0：

| 环节 | 仓库 | 操作前 | 断言结果 |
| --- | --- | --- | --- |
| 全部 Fetch | 6 组件仓 | 各自场景态 | ahead/behind 不变，`--prune` 生效 |
| 批量 Push | module_core | ahead 2 | ahead 0 |
| 批量 Push | FunViews（→`local` 镜像） | ahead 2 | ahead 0 |
| 批量 Pull | module_layout | behind 2 | 0/0，纯 FF 无合并提交，远程文件落到工作区 |
| 分叉 Pull | module_components | 1 ahead / 1 behind | ahead 2 / behind 0，恰有 1 条合并提交，两侧文件并存 |
| 逐仓提交 | module_animations | 脏 3 项 | 提交并推送后脏 0、ahead 0 |
| 新分支首推 | module_frameworks | 无 upstream | upstream=`origin/feature/story-2001-renderer`，远程 tag 可见 |
| 同步仓跳过 | lib_common | 0/0 干净 | push 无操作，HEAD 不变 |
| 远程推送 | FunViews（→GitHub `origin`，SSH） | — | 观测项（不断言）：可达时输出 `Everything up-to-date`，不可达/凭据缺失时输出原始错误文本；两种情况下批量流程都不应中断 |

两处最容易与 git 基准对不上的口径，脚本已单独设断言：

1. **分叉 Pull 后的计数**：merge 得到 ahead=2（1 条本地 + 1 条合并提交）；若 UI 走 rebase，
   应是 ahead=2 且时间线上没有合并提交。
2. **批量 Push 的自动选中集**：初始态应恰好选中壳工程与 module_core；`module_frameworks`
   的特性分支无 upstream，若被计入「未推送提交数」却不随批量 Push 推出新分支，
   说明状态栏口径与操作范围不一致。

## 6. 场景重铺

GitSpace 的一次 Push/Pull/提交会把仓库推离初始态，重铺回初始态：

```bash
bash scripts/seed-scenarios.sh            # 全矩阵重铺（幂等，可反复执行）
bash scripts/report-states.sh --check     # 重铺后对账，退出码 0 表示与矩阵一致
```

脚本做三件事：把每个仓库的工作区、本地分支和裸仓 `main` 一起回退到基线
（各裸仓里的隐藏引用 `refs/baseline/main`），再按矩阵重新生成待推送提交、他人提交、
脏工作区和特性分支/Tag。fixture 源码放在 `scripts/seeds/<仓库>/`，改场景只需改 fixture。

重铺是**确定性**的：每笔场景提交都钉死了 author/committer 日期，因此无论跑多少次，产出的
commit hash 完全相同，远程（含 GitHub `origin`）始终是本地历史的祖先，不会出现
「重铺一次就和远程分叉」。只有改动 fixture 内容或日期才会产生新 hash，此时需要同步
GitHub：`git push -f origin mgit_multi_components`（origin 为 SSH，直接可推）。

注意：重铺会 `checkout -B` 丢弃壳工程与子仓基线之后的本地提交，也会清掉
`module_animations` 的未跟踪动画资源；测试过程中要留档的产物请先移出仓库目录。
基线之后的组件目录改动（例如你自己在 GitSpace 里提交的验证结果）不会被保留。

冲突场景不预置（避免留下半合并状态），需要时按下面手动制造：

```bash
C=$R/module_components
git -C $C checkout -b conflict/price main
printf '\nval conflictLocal = 1\n' >> $C/src/main/java/top/iqqcode/module/components/cart/CartPriceTextView.kt
git -C $C commit -am "conflict: 本地改价格控件"
git -C $C checkout main
printf '\nval conflictRemote = 2\n' >> $C/src/main/java/top/iqqcode/module/components/cart/CartPriceTextView.kt
git -C $C commit -am "conflict: 远程改价格控件"
git -C $C merge conflict/price --no-ff   # 此时处于未解决冲突态
```

`$R=~/Documents/Code/FunViews`；造完冲突后跑一次 `bash scripts/seed-scenarios.sh` 即可回到矩阵初始态。

## 7. 卸载

```bash
bash scripts/reset-demo.sh          # 演练，列出将要删除的项
bash scripts/reset-demo.sh --force  # 删除子仓 .git 与裸仓，壳工程回退到 backup/pre-mgit-split
```

## 8. 已知边界

- 新增的 Kotlin/XML 示例代码按工程既有风格书写，但**未跑过 Gradle 编译**（本机 Android SDK 与
  依赖下载不可用）。对 GitSpace 测试无影响，若要在 Android Studio 里跑起来需自行验证。
- 壳工程 `app/src/main/res/layout/fragment_cart_summary.xml` 引用了 `@color/cart_highlight`，
  该颜色定义在 module_animations 的**未提交**工作区改动里——跨仓耦合失配是有意的观察点。
- 壳工程 `origin` 用 SSH，UI 侧可直接批量 Push 到 GitHub（写路径已实测：临时分支创建+删除均成功）。
  要专门验证「推送失败」的错误路径，把 origin 临时切回 HTTPS——本机钥匙串没有 github.com 凭据、
  git CLI 也不读 macOS 系统代理，因此必然失败：
  `git -C ~/Documents/Code/FunViews remote set-url origin https://github.com/IQQcode/FunViews.git`
  测完切回：`git -C ~/Documents/Code/FunViews remote set-url origin git@github.com:IQQcode/FunViews.git`。
  注意矩阵基准是 `local` 镜像，切换 origin 协议不影响 `--check` 结果。
- 各子仓 `origin` 是 `file://` 本地路径，push/fetch 语义与远程仓一致（GitSpace 走系统 git CLI），
  但不涉及鉴权失败、凭证缺失这类场景。
