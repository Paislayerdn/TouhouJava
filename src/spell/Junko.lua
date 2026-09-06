local spell = {}

local cooldown = 30
local startAmount = 3
local density = 107
local angle = 360/density
local step = -20

spell.onStart = function()
	boss:setMaxHP(50)
end

local bulette = function()
	return spawnBullet(
		sequence(
			addCircleHitbox("bulletHB", 12),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
			var("index", get("i")),
			var("jndex", get("j")),
			var("speed", 2.75),
			warp(999, 999),

			parallel(
				sequence(
					warp(boss),
					look( get( "offset" ) ),
					turn(mul(get("index"), angle/2)),
					turn(mul(get("jndex"), angle)),
					forward(step),

					forever("sequence",
						forward(get("speed"))
					)
				),

				sequence(
					wait(60),
					forever("sequence",
						change("speed", 0.005)
					)
				),

				sequence(
					wait(210),
					destroy()
				)
			)
		)
	)
end

spell.buildAction = function()

return sequence(
	var("offset", 0),
	var("amount", startAmount),

	sound("jingle", "[TH] Jingle"),
	setsoundvolume("jingle", -0.25),

	sound("shot", "[TH] Shot"),
	setsoundvolume("shot", -15.5),
	
	forever("sequence",
		set("offset", mul(random(), 360)),
		jsfor("i", 1, get("amount"), function()
			return sequence(
				playsound("jingle"),
				playsound("shot"),
				jsfor("j", 1, density, function()
					return bulette()
				end),
				wait( 12.5 )
			)
		end),
		change("amount", 1),
		wait(cooldown)
	)
)

end

return spell