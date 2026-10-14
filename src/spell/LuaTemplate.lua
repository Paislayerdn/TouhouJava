local spellData = {}

spellData.configure = {
	name = "NAME HERE",
	timer = 60*120,
	playerCandidateRadius = 70,
	countableTime = timer,
	isSpell = false,
	caster = "LAMBDA"
}

spellData.onStart = function()
	boss:setMaxHP(100)
	spell:startTimer()
	spell:startCounting()
end



spellData.buildAction = function()
	return sequence(
		
	)
end

return spellData