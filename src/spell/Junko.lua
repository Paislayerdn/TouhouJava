local spellData = {}

local cooldown = 30
local startAmount = 3
local density = 59
local angle = 360/density

spellData.configure = {
	name = "Replicated [Trembling, Shivering Star]",
	timer = 30*60,
	playerCandidateRadius = 30,
	isSpell = true,
	scoreReward = 10000000,
	caster = "LAMBDA",
	survival = true
}

spellData.onStart = function()
	boss:setMaxHP(50)
	spell:startTimer()
end

local bulette = function()
	return spawnBullet( sequence(
		replicate("i", "j"),
		var("speed", 4.5),
		setCostume("BulletCircle"),
		setSize(11.5),
		setBrightness(100),
		addCircleHitbox("bulletHB", 3),
		addHitboxTag("bulletHB", "ENEMY_BULLET"),
		warp(999, 999),

		parallel(
			sequence(
				warp(boss),
				look( get( "offset" ) ),
				turn(mul(get("i"), angle/2)),
				turn(mul(get("j"), angle)),
				forward(-20),

				forever("sequence",
					forward(get("speed")),
					wait()
				)
			),

			sequence(
				wait(60),
				forever("sequence",
					change("speed", 0.04),
					wait()
				)
			),

			sequence(
				wait(300),
				destroy()
			)
		)
	) )
end

spellData.buildAction = function()
	return sequence(
		var("offset", 0),
		var("amount", startAmount),

		sound("jingle", "[TH] Jingle", -0.25),
		sound("shot", "[TH] Shot", -15.5),

		forever("sequence",
			set("offset", mul(random(), 360)),
			jsfor("i", 1, get("amount"), function()
				return sequence(
					playSound("jingle"),
					playSound("shot"),
					jsfor("j", 1, density, function()
						return bulette()
					end),
					wait( 15 )
				)
			end),
			change("amount", 0.5),
			wait(cooldown)
		)
	)
end

return spellData
