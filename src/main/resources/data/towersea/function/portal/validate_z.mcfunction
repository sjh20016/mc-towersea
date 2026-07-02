# Z型传送门 (沿X轴): 检查14个位置的青金石块
execute unless block ~ ~0 ~1 lapis_block run return
execute unless block ~ ~0 ~-1 lapis_block run return
execute unless block ~ ~4 ~1 lapis_block run return
execute unless block ~ ~4 ~-1 lapis_block run return
execute unless block ~ ~1 ~2 lapis_block run return
execute unless block ~ ~2 ~2 lapis_block run return
execute unless block ~ ~3 ~2 lapis_block run return
execute unless block ~ ~1 ~-2 lapis_block run return
execute unless block ~ ~2 ~-2 lapis_block run return
execute unless block ~ ~3 ~-2 lapis_block run return
execute unless block ~1 ~0 ~1 lapis_block run return
execute unless block ~-1 ~0 ~1 lapis_block run return
execute unless block ~1 ~0 ~-1 lapis_block run return
execute unless block ~-1 ~0 ~-1 lapis_block run return
# 验证通过，创建传送门
function towersea:portal/create_z
