# 简化：如果 marker 附近没有青金石块则销毁
execute at @s unless block ~1 ~1 ~ lapis_block unless block ~-1 ~1 ~ lapis_block unless block ~ ~1 ~1 lapis_block unless block ~ ~1 ~-1 lapis_block run function towersea:portal/destroy
