package mac.yk.devicemanagement.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

public class UtilDateDeserializer implements JsonDeserializer<Date> {



	@Override
    public Date deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		
		return new  java.util.Date(arg0.getAsJsonPrimitive().getAsLong());
	}

}