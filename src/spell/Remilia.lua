local spellData = {}

local count = {6, 8, 5}

spellData.configure = {
	name = "Remilia's First Nonspell",
	timer = 90*60,
	playerCandidateRadius = 70,
	isSpell = false,
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
		jsfor("i", 1, at(count, 1), function()
			return spawnBullet(
				sequence(
					var("index", get("i")),
					setGhost(100),
					setCostume("BulletDonut"),
					addCircleHitbox("bulletHB", 15),
					addHitboxTag("bulletHB", "ENEMY_BULLET"),
					addHitboxTag("bulletHB", "CLEARABLE"),
					var("speed", 9),

					parallel(
						sequence(
							warp(boss),
							look( get("offset") ),
							turn( mul(get("index"), div( 360, at(count, 1) ) ) ),
							turn( mul(21, get("count"), get("direction")) ),
							forever("sequence",
								forward( get("speed") ),
								wait()
							)
						),
						tween("size", 100, 60,
							"brightness", 0,
							"ghost", 100, 0,
							20, easing.linear),		
						forever("sequence",
							change("speed", 0.06),
							wait()
						),
						sequence(
							wait(270),
							disableHitbox("bulletHB"),
							tween("ghost", 100, 60),
							destroy()
						)
					)
				)
			)
		end)
	)
end

wave[2] = function()
	return sequence(
		jsfor("i", 1, at(count, 2), function()
			return spawnBullet(
				sequence(
					var("index", get("i")),
					setGhost(100),
					setDesaturation(40),
					setColor(160),
					setCostume("BulletRinged"),
					addCircleHitbox("bulletHB", 6),
					addHitboxTag("bulletHB", "ENEMY_BULLET"),
					addHitboxTag("bulletHB", "CLEARABLE"),

					parallel(
						sequence(
							warp(boss),
							look( get("offset") ),
							turn( mul(get("index"), div( 360, at(count, 2) ) ) ),
							turn( mul(360/8+19, get("count"), get("direction")) ),
							forever("sequence",
								forward(5),
								wait()
							)
						),
						tween("size", 100, 20,
							"brightness", 100, 60,
							"ghost", 100, 0,
							20, easing.linear),		

						sequence(
							wait(270),
							disableHitbox("bulletHB"),
							tween("ghost", 100, 60),
							destroy()
						)
					)
				)
			)
		end)
	)
end

wave[3] = function()
	return sequence(
		jsfor("i", 1, count[3], function()
			return spawnBullet(
				sequence(
					var("index", get("i")),
					setGhost(100),
					setPixelate(5),
					setColor(190),
					setCostume("BulletFog"),

					addCircleHitbox("bulletHB", 4),
					addHitboxTag("bulletHB", "ENEMY_BULLET"),
					addHitboxTag("bulletHB", "CLEARABLE"),

					parallel(
						sequence(
							warp(boss),
							look( get("offset") ),
							turn( mul(get("count"), -360/10, get("direction") ) ),
							turn( mul(get("index"), 4) ),
							forever("sequence",
								forward(7),
								wait()
							)
						),
						sequence(
							tween("size", 60, 30,
								"ghost", 100, 0,
								30, easing.linear),		
							setCostume("BulletKunai")
						),
						sequence(
							wait(180),
							disableHitbox("bulletHB"),
							destroy()
						)
					)
				)
			)
		end)
	)
end

spellData.buildAction = function()
	return sequence(
		sound("jingle", "[TH] Jingle"),
		setSoundVolume("jingle", -10.25),

		sound("shot", "[TH] Shot"),
		setSoundVolume("shot", -7.5),
		
		var("offset", 0),
		var("count", 0),
		var("direction", 1),
		forever("sequence",
			set("offset", mul(random(), 360)),
			set("direction", mul(get("direction"), -1)),
			jsfor("fire", 1, random(60,80), function()
				return sequence(
					playSound("jingle"),
					playSound("shot"),
					jsfor("wave", 1, 3, function()
						return callAt( wave, get("wave") )
					end),
					change("count", 1),
					wait( 10 )
				)
			end),
			wait(60)
		)
	)
end

return spellData