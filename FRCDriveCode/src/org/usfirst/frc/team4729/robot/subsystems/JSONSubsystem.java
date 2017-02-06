package org.usfirst.frc.team4729.robot.subsystems;

import org.json.simple.JSONObject;

public class JSONSubsystem {
	public String encodeJSON(String p1, String p2, String p3) {
		JSONObject object = new JSONObject();
		object.put("p1", p1);
		object.put("p2", p2);
		object.put("p3", p3);
		return object;
	}
}