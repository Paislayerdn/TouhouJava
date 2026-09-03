local count = 350
local step = 2.25
local goldenAngle = 360 * (1 - 2 / (1 + math.sqrt(5)))

return forever("sequence",
	var("offset", mul(random(), 360)),

	sound("jingle", "[TH] Jingle"),
	setsoundvolume("jingle", -0.25),
	playsound("jingle"),

	sound("shot", "[TH] Shot"),
	setsoundvolume("shot", -27.5),

	jsfor("i", 1, count, function()
		return spawnBullet(
			sequence(
				addCircleHitbox("bulletHB", 12),
				addHitboxTag("bulletHB", "ENEMY_BULLET"),
				var("index", get("i")),
				var("speed", 0.15),
				warp(999, 999),
				wait( add( mul(get("index"), 0.35) ) ),

				parallel(
					sequence(
						warp(boss),
						look( get("offset") ),
						turn(mul(get("index"), goldenAngle)),
						forward(mul(get("index"), step)),
						playsound("shot"),

						forever("sequence",
							forward(get("speed"))
						)
					),

					sequence(
						wait(35),

						forever("sequence",
							change("speed", -0.035)
						)
					),

					sequence(
						wait(360),
						destroy()
					)
				)
			)
		)
	end),
	wait(150)
)