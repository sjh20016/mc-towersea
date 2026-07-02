# 检测 9x6x3 范围内的青金石块框架
# 执行 X 型和 Z 型验证
execute if block ~1 ~1 ~ lapis_block run function towersea:portal/validate_x
execute if block ~-1 ~1 ~ lapis_block run function towersea:portal/validate_x
execute if block ~ ~1 ~1 lapis_block run function towersea:portal/validate_z
execute if block ~ ~1 ~-1 lapis_block run function towersea:portal/validate_z
