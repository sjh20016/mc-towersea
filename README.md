# TowerSea（天井塔海）

Minecraft Java 1.20.1 Forge Mod — 数据包优先的塔海维度。

## 环境要求

- Minecraft 1.20.1
- Forge 47.x
- Java 17

## 架构概述

本项目采用"数据包优先"架构：

- **Java Mod** 仅负责维度注册、自定义数据校验、调试命令和备用安全网
- **数据包** 驱动世界生成（32 生物群系 multi_noise）、传送门、青金石桥、坠落回收、魔法系统
- 新增塔型、建筑、群系时，只需修改数据包，不改 Java

### 世界参数

| 参数 | 值 |
|------|-----|
| 世界高度 | 1280（Y: -256 ~ 1023） |
| 生物群系数量 | 32（28 主群系 + 4 后备） |
| 塔体生成 | 双塔体系统（coarse_spire + needle_spire） |
| 裂缝/空洞 | spire_crack + air_lane 密度削减 |
| 坠落阈值 | Y <= -240 |
| 返回点 | 4 个随机主世界返回点 |
| 安全 buff | 缓降 20s + 抗性 8s |

## 快速开始

### 安装 Mod

1. 构建项目：`./gradlew build`
2. 将 `build/libs/towersea-1.0.0.jar` 放入 `.minecraft/mods/`
3. 启动 Forge 1.20.1

### 安装数据包

Mod 内置基础数据包。将 `towersea_dev` 额外数据包放入存档的 `datapacks/` 目录可覆盖/扩展配置。

进入游戏后执行 `/reload` 生效。

### 进入塔海维度

1. 用青金石块搭建传送门框架（X 型或 Z 型，14 块青金石）
2. 使用打火石点燃框架
3. 内部填充 light 方块后走入传送门
4. 进入后到达 (0, 340, 12)

### 进入仓库/实验维度

```
/towersea lab       # 结构实验维度 (0, 100, 0)
```

## 调试命令

| 命令 | 功能 |
|------|------|
| `/towersea lab` | 进入结构实验维度 |
| `/towersea tp <dimension>` | 传送到指定维度 |
| `/towersea reload` | 重新加载自定义 JSON 配置 |
| `/towersea validate` | 验证所有 towersea JSON 配置 |
| `/towersea structure place <id>` | 在前方放置指定 NBT 结构 |
| `/towersea structure list` | 列出已加载结构 |
| `/towersea profile list` | 列出所有塔体配置 |
| `/towersea profile inspect <id>` | 查看塔体配置详情 |
| `/towersea debug chunk` | 显示当前区块坐标和高度 |
| `/towersea debug connectors` | 显示附近连接点信息 |

## 传送门系统

传送门由数据包函数驱动：

- 打火石使用 → advancement 触发 → mcfunction 扫描青金石框架
- X 型（沿 Z 轴）：14 块青金石验证，填充 3x1 light 方块
- Z 型（沿 X 轴）：14 块青金石验证，填充 1x3 light 方块
- 传送门每刻检测框架完整性，被破坏自动清除
- 传送冷却 80 tick

## 青金石桥系统

- 骨粉使用 oak_sapling → advancement 触发 → raycast + 仪式验证
- Marker 实体驱动逐格建造（水平桥最多 60 格）
- 支持水平桥和楼梯桥两种模式

## 32 个生物群系

| 生物群系 | 色调 | 特色 |
|----------|------|------|
| amethyst_rime_belfries | 冰蓝冷色 | 紫水晶、方解石、冰 |
| verdant_cascade_borough | 翠绿亮 | 泥、苔藓、繁花 |
| gloamcap_towergrove | 菌紫暗 | 蘑菇、菌丝 |
| stormwing_watch | 冷灰 | 深板岩、铜块、幻翼 |
| cloudstep_goatlands | 绿蓝 | 山羊、草地 |
| chainlight_citadel | 灰绿 | 石砖、苔石砖 |
| powder_snow_sink | 白冷 | 细雪、雪块 |
| chorus_void_garden | 紫暗 | 紫颂、末地石 |
| mycelium_shelf_isles | 菌紫 | 无怪物、蘑菇 |
| inverted_dripstone_vault | 灰绿 | 滴水石锥 |
| crimson_thermal_well | 红暗 | 岩浆、绯红菌 |
| echo_sculk_spires | 黑暗 | 幽匿、黑石 |
| frostneedle_roost | 冰蓝 | 蓝冰、山羊 |
| saltstone_drift_arches | 沙白 | 砂岩、方解石 |
| lantern_vine_orchard | 绿亮 | 苔藓、藤蔓 |
| sporefall_mosswell | 绿暗 | 繁花洞穴植被 |
| lapis_echo_hollow | 深蓝 | 凝灰岩、深板岩 |
| glasswind_cloud_reef | 半透冰蓝 | 蓝冰、紫水晶 |
| copper_sail_cliffs | 橙褐 | 铜块、山羊 |
| windroot_shelf_woodland | 绿棕 | 苔藓、丛林树 |
| suncracked_wall_garden | 沙红 | 红沙、枯灌木 |
| basalt_root_chasm | 暗棕 | 玄武岩、黑石 |
| high_snow_spire | 纯白冷 | 雪块、山羊 |
| pale_calcite_spire | 白亮 | 方解石、石英 |
| sky_garden_spire | 天蓝绿 | 草地、橡树 |
| cloud_grassland | 天蓝 | 羊、鸡 |
| mosslit_hanging_forest | 翠绿 | 苔藓、丛林植被 |
| moss_shaft | 绿暗 | 繁花洞穴 |
| sandstone_spire | 沙红 | 红沙、枯灌木 |
| deep_shaft | 极暗 | 玄武岩、黑石 |
| warehouse | 灰 | 无怪物 |
| structure_lab | 灰 | 无怪物 |

## 数据包结构

```
data/towersea/
  dimension/                 # 塔海维度 + 仓库维度
  dimension_type/            # 维度类型（1280高度、低环境光）
  worldgen/
    biome/                   # 32个自定义生物群系
    noise/                   # 11个自定义噪声
    noise_settings/          # 噪声设置（双塔体密度函数）
    density_function/towersea/ # 13个密度函数
    configured_feature/       # 配置特性
    placed_feature/           # 放置特性
    structure/                # Jigsaw 结构
    structure_set/            # 结构集
    template_pool/            # 结构池
  towersea/
    tower_profiles/           # 塔体配置
    height_bands/             # 高度层配置
    bridge_rules/             # 桥梁规则
    structure_categories/     # 结构分类
    portal_rules/             # 传送门规则
    world_rules/              # 世界规则
  function/                  # 所有 mcfunction 逻辑
    portal/                   # 传送门系统
    safety/                   # 安全/坠落回收
    magic/
      common/                 # 魔法通用系统
      bridge/                 # 青金石桥系统
  advancement/                # 传送门触发器 + 桥触发器
  predicates/                 # 坠落检测谓词
  tags/                       # 方块标签
  loot_tables/                # 战利品表
  recipes/                   # 合成配方
```

## 噪声生成管线

```
coarse_spire_mask (2D) ──┐
                         ├──> max ──> spire_density ──> final_density
needle_spire_mask (2D)  ──┘         │
spire_detail (3D) ───> 细化密度      │
spire_hollow (3D) ───> 镂空          ├──> spire_crack (裂缝削减 -1.75)
spire_crack (2D+Y) ──────────────┐  │
air_lane (2D)     ──────────────┴──> air_lane_hole (空中走廊 -1.08)
layer_gate_root/main/high/floating ──> 高度分层门控
height_gate ──> 世界上下硬边界 (Y:-256~-248, Y:1016~1023)
```

## 重新测试地形生成

1. 退出世界
2. 删除 `dimensions/towersea/towersea/region/` 下的区块文件
3. 重新进入世界
4. 新区块将使用当前数据包规则生成

## 排查加载失败

- 查看日志中 `[TowerSea]` 前缀的信息
- 执行 `/towersea validate` 检查 JSON 配置
- 确认所有 JSON 格式正确且字段完整
- 检查 noise/density_function 之间的引用链是否完整
- 无效配置会跳过并输出错误日志，不会损坏存档

## 结构制作流程

1. `/towersea lab` 进入结构实验维度
2. 使用 Axiom/WorldEdit/Structure Block 建造
3. Structure Block 保存为 .nbt
4. 放入 `data/towersea/structures/<category>/`
5. 修改对应 template_pool JSON
6. `/reload` 重载
7. 在实验维度用 `/towersea structure place towersea:<category>/<name>` 测试

## 结构命名规范

```
towersea:anchor/anchor_tower_01
towersea:tower/small_tower_01
towersea:tower/broken_tower_01
towersea:bridge/rope_bridge_01
towersea:village/platform_01
towersea:ruin/side_shrine_01
towersea:dungeon/hollow_core_01
```

禁止无意义命名（如 test1、newtower、aaa）。

## Java 模块结构

```
com.towersea/
  TowerSeaMod.java            # Mod 主入口
  registry/                    # 注册表
  world/
    dimension/                 # 维度 Key 定义
    generator/                 # 地形生成辅助
    feature/                   # 自定义 Feature（塔体）
    structure/                 # 结构支持
    bridge/                    # 桥梁底层
  portal/                      # 传送门工具方法
  data/
    reload/                    # 自定义 JSON 热重载
    codec/                     # Codec 定义（6种配置类型）
    validation/                # 校验逻辑
  player/                      # 坠落回收备用安全网
  command/                     # /towersea 调试命令
  network/                     # 网络通道
  client/                      # 客户端事件
  util/                        # 工具方法
  debug/                       # 调试辅助
```
