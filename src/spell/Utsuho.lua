local spellData = {}

local cooldown = 45
local interval = 2
local wave = 90
local density = 42
local tur = 90

spellData.configure = {
	name = "Utsuho 2nd nonspell",
	timer = 60*60,
	playerCandidateRadius = 60,
	isSpell = false,
	caster = "LAMBDA",
	survival = false
}

spellData.onStart = function()
	boss:setMaxHP(50)
	spell:startTimer()
	spell:startCounting()
end

local bulette = function()
	return spawnBullet( sequence(
		replicate("i", "j", "LR"),
		set("LR", mul( get("LR") , 2)  ),
		change("LR", -1),
		change("offset", mul(0.01, get("dir")) ),
		var("speed", 13.5),
		setCostume("BulletRinged"),
		setSize(25),
		jsif( equal( get("LR"), get("dir") ),
			function() return setColor(230) end,
			function() return setColor(45) end
		),
		setBrightness(100),
		setGhost(100),
		addCircleHitbox("bulletHB", 14),
		addHitboxTag("bulletHB", "ENEMY_BULLET", "CLEARABLE"),
		warp(999, 999),

		parallel(
			sequence(
				warp(boss),
				look( get( "offset" ) ),
				turn( mul(get("j"), 360/(density/2) ) ),

				forever("sequence",
					forward(get("speed")),
					wait()
				)
			),

			tween("size", 48, 25,
				"ghost", 100, 0,
				"brightness", 100, 80, 30),
			tween("desaturation", 0, 100,
				"pixelate", 0, 25,
				90, easing.quadIn),

			sequence(
				wait(6),
				turn( mul(get("LR"), tur) , 70),
				wait(60),
				destroy()
			)
		)
	) )
end

spellData.buildAction = function()
	return sequence(
		var("offset", 0),
		var("dir", -1),

		sound("jingle", "[TH] Jingle", -0.25),
		sound("shot", "[TH] Shot", -15.5),

		forever("sequence",
			set("offset", 0),
			set("dir", mul(get("dir"), -1) ),
			playSound("jingle"),
			jsfor("i", 1, wave, function()
				return sequence(
					playSound("shot"),
					jsfor("LR", 0,1, function()
						return jsfor("j", 1, density/2, function()
							return bulette()
						end)
					end),
					wait( interval )
				)
			end),
			wait(cooldown)
		)
	)
end

return spellData
