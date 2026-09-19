local spellData = {}

local cooldown = 30
local startAmount = 3
local density = 107
local angle = 360/density
local step = -20

spellData.config = {
	name = "Junko",
	timer = 120*60,
	playerCandidateRadius = 30,
	isSpell = false
}

spellData.onStart = function()
	boss:setMaxHP(50)
	spell:startTimer()
	spell:startCounting()
end

local bulette = function()
	return spawnBullet(
		sequence(
			var("index", get("i")),
			var("jndex", get("j")),
			var("speed", 2.75),
			setCostume("CircleBullet"),
			setSize(10),
			setBrightness(100),
			addCircleHitbox("bulletHB", 8),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
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

spellData.buildAction = function()

return sequence(
	var("offset", 0),
	var("amount", startAmount),

	sound("jingle", "[TH] Jingle"),
	setSoundVolume("jingle", -0.25),

	sound("shot", "[TH] Shot"),
	setSoundVolume("shot", -15.5),
	
	forever("sequence",
		set("offset", mul(random(), 360)),
		jsfor("i", 1, get("amount"), function()
			return sequence(
				playSound("jingle"),
				playSound("shot"),
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

return spellData