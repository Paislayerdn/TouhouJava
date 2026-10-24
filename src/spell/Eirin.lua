local spellData = {}

local rings = 17
local density = 19
local initialSpeed = 2
local angle1, angle2 = 360/rings, 360/density
local cooldown = 175

spellData.configure = {
	name = "Eirin nonspell",
	timer = 120*60,
	playerCandidateRadius = 35,
	isSpell = false,
	caster = "LAMBDA"
}

spellData.onStart = function()
	boss:setMaxHP(50)
	spell:startTimer()
	spell:startCounting()
end

local bullette = function()
	return spawnBullet( sequence(
		replicate("i", "j"),
		var("speed", initialSpeed),
		var("colorOffset", add( 120, get("j") )  ),

		setGhost(100),
		setColor( random(40, 210) ),
		setCostume("BulletFog"),
		addCircleHitbox("bulletHB", 5),
		addHitboxTag("bulletHB", "ENEMY_BULLET", "CLEARABLE"),

		parallel(
			sequence(
				warp(boss),
				look( get( "offset" ) ),
				turn(mul(get("i"), angle1)),
				forward(110),

				look(0),
				turn(mul(get("j"), angle2)),
				forward(30),
				forever("sequence",
					forward(get("speed")),
					wait()
				)
			),
			sequence(
				tween("size", 40, 16,
					"color", get("colorOffset"),
					"brightness", 100, 10,
					"ghost", 100, 0,
					add(70, mul(get("i"), 5) ,mul(get("j"), 1.5)), easing.quadInOut),		
				setCostume("BulletRinged")
			),
			sequence(
				wait(1),
				turn( mul( get("dir"), 45), 29 ),
				forever("sequence",
					change("speed", -0.1),
					wait()
				)
			),

			sequence(
				wait(360),
				disableHitbox("bulletHB"),
				tween("ghost", 100, 60),
				destroy()
			)
		)
	) )
end

spellData.buildAction = function()
	return sequence(
		var("offset", mul(random(), 360)),
		var("dir", -1),

		sound("jingle", "[TH] Jingle", -0.25),
		sound("shot", "[TH] Shot", -15.5),

		forever("sequence",
			playSound("jingle"),
			playSound("shot"),
			set("dir", mul(get("dir"), -1) ),
			jsfor("i", 1, rings, function()
				return jsfor("j", 1, density, function()
					return bullette()
				end)
			end),
			wait(cooldown)
		)
	)
end

return spellData
