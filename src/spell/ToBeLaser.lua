local rings = 16
local density = 32
local step = 2.25
local angle1, angle2 = 360/rings, 360/density
local speed = 3

local Lasseree = function()
	return spawnBullet(
		sequence(
			
			parallel(
				sequence(
					warp(boss),
					lookTowards(player),

					forever("sequence",
						forward(10)
					)
				),

				sequence(
					wait(30),
					destroy()
				)
			)
		)
	)
end

local CasualWalk = function()
	return spawnBullet(
		sequence(
			parallel(
				sequence(
					warp(240,-240),
					lookTowards(boss),

					forever("sequence",
						forward(speed)
					)
				),

				forever("sequence",
					wait(2),
					Lasseree()
				),

				sequence(
					wait(360),
					destroy()
				)
			)
		)
	)
end

return forever("sequence",
	sound("jingle", "[TH] Jingle"),
	setsoundvolume("jingle", -0.25),
	playsound("jingle"),

	sound("shot", "[TH] Shot"),
	setsoundvolume("shot", -15.5),
	playsound("shot"),
	CasualWalk(),
	wait(10)
)