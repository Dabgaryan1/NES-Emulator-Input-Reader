local frame = 0 -- Initialize a frame counter

local dataFolder = emu.getScriptDataFolder()

if dataFolder == "" then
	emu.log("Error: Enable 'Allow access to I/O and OS functions'")
	return
end

local filePath = dataFolder .. "/input_stream.csv"
local outputFile = io.open(filePath, "w")

if outputFile == nil then
	emu.log("Error: Could not create input file: " .. filePath)
	return
end

-- Column names
outputFile:write("Frame,Up,Down,Left,Right,A,B,Start,Select\n")
outputFile:flush()

-- Converts true/false into 1/0 for CSV
local function boolToInt(value)
	if value then
		return 1
	else
		return 0
	end
end

-- Called once after Mesen polls input for a frame
function onInputPolled()
	frame = frame + 1

	local input = emu.getInput(0)

	local line = string.format(
		"%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
		frame,
		boolToInt(input.up),
		boolToInt(input.down),
		boolToInt(input.left),
		boolToInt(input.right),
		boolToInt(input.a),
		boolToInt(input.b),
		boolToInt(input.start),
		boolToInt(input.select)
	)

	outputFile:write(line)
	outputFile:flush()
end

emu.addEventCallback(
	onInputPolled,
	emu.eventType.inputPolled
)

emu.log("Writing input stream to: " .. filePath)