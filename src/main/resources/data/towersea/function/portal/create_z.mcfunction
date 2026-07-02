# 填充 Z 型传送门内部为 light 方块
fill ~ ~1 ~1 ~ ~3 ~-1 minecraft:light[level=11] replace minecraft:air
fill ~ ~1 ~1 ~ ~3 ~-1 minecraft:light[level=11] replace minecraft:cave_air
# 生成 portal marker
summon marker ~ ~2 ~ {Tags:["towersea_portal_marker","portal_z"]}
