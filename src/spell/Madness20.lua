local spellData = {}

local count = {39, 11, 9, 9}
local w2bloom = 10
local w2spike = 9
local w2mid = (w2spike+1)/2
local w3bloom = 29
local cooldowns = {20, 5, 15, 10}

spellData.configure = {
	name = "Touhou 20 Stage 5 Midboss bs idk wth is that holy cow",
	timer = 60*60,
	playerCandidateRadius = 70,
	isSpell = true,
	caster = "LAMBDA"
}

spellData.onStart = function()
	boss:setMaxHP(50)
	spell:startTimer()
	spell:startCounting()
end
local wave = {}

wave[1] = function()
return sequence(
	jsfor("j", 1, count[1], function()
		return sequence(
			jsfor("k", 1, random(5, 8), function()
				return spawnBullet(
					sequence(
						setCostume("CircleBullet"),
						setColor(5),
						addCircleHitbox("bulletHB", 16),
						addHitboxTag("bulletHB", "ENEMY_BULLET"),
						addHitboxTag("bulletHB", "CLEARABLE"),
						var("speed", 3),
						change("speed", mul(0.15, random(-10,10))),
						playSound("shot"),

						parallel(
							sequence(
								warp(boss),
								look( mul(random(), 360) ),
								forward(-25),
								forever("sequence",
									forward(get("speed")),
									change("speed", 0.025)
								)
							),
							tween("size", 60, 25,
								"brightness", -100, 10,
								"ghost", 100, 0,
								25, easing.linear),		

							sequence(
								wait(180),
								destroy()
							)
						)
					)
				)
			end),
			wait( 2 )
		)
	end)
)
end

local function wave2helper()
return jsfor("m", 1, w2spike, function()
	return spawnBullet(
		sequence(
			var("jndex", get("j")),
			var("kndex", get("k")),
			var("mndex", get("m")),
			change("mndex", -w2mid),
			var("mirror", abs(get("mndex"))),
			set("mirror", sub(w2mid, get("mirror"))),
			setCostume("OvalBullet"),
			setColor(130),
			setBrightness(100),
			addCircleHitbox("bulletHB", 7),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
			addHitboxTag("bulletHB", "CLEARABLE"),
			var("speed", 6.5),
			change("speed", mul(get("mirror"), 0.34)),

			parallel(
				sequence(
					warp(boss),
					look( get("offset") ),
					turn( mul(get("jndex"), 360/(w2bloom*2)) ),
					forward(40),

					turn( get("offset2") ),
					turn( mul(get("kndex"), 360/w2bloom) ),
					turn( mul(get("mndex"), 0.9) ),
					forward(-40),
					forever("sequence",
						forward(get("speed"))
					)
				),
				tween("size", 30, 15,
					"brightness", sub(-30, mul(get("mirror"), -20) ),
					"ghost", 100, 0,
					25, easing.linear),		

				sequence(
					wait(180),
					destroy()
				)
			)
		)
	)
end)
end

wave[2] = function()
return sequence(
	var("offset2", 0),
	jsfor("j", 1, count[2], function()
		return sequence(
			change("offset2", 36),
			playSound("shot"),
			jsfor("k", 1, w2bloom, function()
				return wave2helper()
			end),
			wait( 10 )
		)
	end)
)
end

wave[3] = function()
return sequence(
	jsfor("j", 1, count[3], function()
		return sequence(
			jsfor("k", 1, w3bloom, function()
				return spawnBullet(
					sequence(
						var("jndex", get("j")),
						var("kndex", get("k")),
						setCostume("CircleBullet"),
						setColor(70),
						addCircleHitbox("bulletHB", 15),
						addHitboxTag("bulletHB", "ENEMY_BULLET"),
						addHitboxTag("bulletHB", "CLEARABLE"),
						var("speed", 7),
						playSound("shot"),

						parallel(
							sequence(
								warp(boss),
								look( get("offset") ),
								turn( mul(get("kndex"), 360/w3bloom ) ),
								turn( mul(get("jndex"), 360/(w3bloom*2) ) ),
								forward(-60),
								forever("sequence",
									forward(get("speed")),
									change("speed", 0.05)
								)
							),
							tween("size", 60, 35,
								"brightness", -100, 60,
								"ghost", 100, 0,
								30, easing.linear),		

							sequence(
								wait(180),
								destroy()
							)
						)
					)
				)
			end),
			wait( 15 )
		)
	end)
)
end
wave[4] = function()
return sequence(
	var("chosen", mul(360, random())),
	jsfor("j", 1, count[4], function()
		return sequence(
			set("chosen", mul(360, random())),
			jsfor("k", 1, 25, function()
				return spawnBullet(
					sequence(
						setCostume("CircleBullet"),
						setColor(210),
						addCircleHitbox("bulletHB", 13),
						addHitboxTag("bulletHB", "ENEMY_BULLET"),
						addHitboxTag("bulletHB", "CLEARABLE"),
						var("speed", add( -1, mul(8, random()) )  ),
						playSound("shot"),

						parallel(
							sequence(
								warp(boss),
								look( get("chosen") ),
								forward(60),

								look( mul(360, random()) ),
								forward(90),
								forever("sequence",
									forward(get("speed")),
									change("speed", 0.025)
								)
							),
							tween("size", 120, 30,
								"brightness", -100, 60,
								"ghost", 100, 0,
								25, easing.quadInOut),		

							sequence(
								wait(180),
								tween("ghost", 100, 90),
								destroy()
							)
						)
					)
				)
			end),
			wait( 5 )
		)
	end)
)
end

spellData.buildAction = function()

return sequence(
	var("offset", mul(random(), 360)),
	var("dir", -1),

	sound("jingle", "[TH] Jingle"),
	setSoundVolume("jingle", -0.25),

	sound("shot", "[TH] Shot"),
	setSoundVolume("shot", -15.5),

	forever("sequence",
		jsfor("i", 1, 4,
			function() return sequence(
				callAt(wave, get("i")),
				wait( at(cooldowns, get("i"))  )
			)
		end)
	)
)

end

return spellData