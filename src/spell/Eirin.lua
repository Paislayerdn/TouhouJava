local rings = 16
local density = 32
local step = 2.25
local angle1, angle2 = 360/rings, 360/density

return forever("sequence",
	var("offset", mul(random(), 360)),

	sound("jingle", "[TH] Jingle"),
	setsoundvolume("jingle", -0.25),
	playsound("jingle"),

	sound("shot", "[TH] Shot"),
	setsoundvolume("shot", -15.5),
	playsound("shot"),

	jsfor("i", 1, rings, function()
		return jsfor("j", 1, density, function()
			return spawnBullet(
				sequence(
					addCircleHitbox("bulletHB", 12),
					addHitboxTag("bulletHB", "ENEMY_BULLET"),
					var("index", get("i")),
					var("jndex", get("j")),
					var("speed", 1.25),
					warp(999, 999),

					parallel(
						sequence(
							warp(boss),
							look( get( "offset" ) ),
							turn(mul(get("index"), angle1)),
							forward(110),

							look(0),
							turn(mul(get("jndex"), angle2)),
							forward(30),

							forever("sequence",
								forward(get("speed"))
							)
						),

						sequence(
							wait(30),
							turn( 45 ),
							forever("sequence",
								change("speed", -0.025)
							)
						),

						sequence(
							wait(360),
							destroy()
						)
					)
				)
			)
		end)
	end),
	wait(210)
)