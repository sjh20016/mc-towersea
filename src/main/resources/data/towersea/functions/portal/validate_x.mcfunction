# X型传送门 (沿Z轴): 检查14个位置的青金石块
# 底面4个 + 顶面4个 + 两侧各3个
execute unless block ~1 ~0 ~ lapis_block run return
execute unless block ~-1 ~0 ~ lapis_block run return
execute unless block ~1 ~4 ~ lapis_block run return
execute unless block ~-1 ~4 ~ lapis_block run return
execute unless block ~2 ~1 ~ lapis_block run return
execute unless block ~2 ~2 ~ lapis_block run return
execute unless block ~2 ~3 ~ lapis_block run return
execute unless block ~-2 ~1 ~ lapis_block run return
execute unless block ~-2 ~2 ~ lapis_block run return
execute unless block ~-2 ~3 ~ lapis_block run return
execute unless block ~1 ~0 ~1 lapis_block run return
execute unless block ~1 ~0 ~-1 lapis_block run return
execute unless block ~-1 ~0 ~1 lapis_block run return
execute unless block ~-1 ~0 ~-1 lapis_block run return
# 验证通过，创建传送门
function towersea:portal/create_x
