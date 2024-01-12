/**
 * 
 */
package json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

// create class to validate JSON document 
@ManagedBean
@RequestScoped
public class JsonValidatorExampleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message,errorMessage;
	private String schema = "";/*"{\r\n"
			+ "  \"$schema\": \"https://json-schema.org/draft/2019-09/schema\",\r\n"
			+ "  \"type\": \"object\",\r\n"
			+ "  \"properties\": {\r\n"
			+ "    \"x\": {\r\n"
			+ "      \"type\":\"string\",\r\n"
			+ "      \"$ref\": \"https://github.com/adewg/ICAR/blob/3340b2041f73afe47079426588bcef1eaf2b004e/enums/icarDepartureKindType.json\"\r\n"
			+ "    }\r\n"
			+ "  }\r\n"
			+ "}";*/
	private String data = "";/*"{\r\n"
			+ "  \"x\":\"X\"\r\n"
			+ "}";*/

	public JsonValidatorExampleBean() {
		try {
			StringBuilder sb = new StringBuilder();
			InputStream in = getClass().getResourceAsStream("LEI/eventCore.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + System.lineSeparator());
			}
			schema = sb.toString();
		} catch (Exception e) {
		}
	}

	public void ok() {

		// create instance of the ObjectMapper class
		ObjectMapper objectMapper = new ObjectMapper();

		// create an instance of the JsonSchemaFactory using version flag
		JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);

		// store the JSON data in InputStream
		try {
			// InputStream jsonStream = new FileInputStream( data );
			//InputStream schemaStream = getClass().getResourceAsStream("LEI/LEIEventCore.json");

			// read data from the stream and store it into JsonNode
			JsonNode json = objectMapper.readTree(data);

			// get schema from the schemaStream and store it into JsonSchema
			JsonSchema jschema = schemaFactory.getSchema(schema);

			// create set of validation message and store result in it
			Set<ValidationMessage> validationResult = jschema.validate(json);

			// show the validation errors
			if (validationResult.isEmpty()) {

				// show custom message if there is no validation error
				message = "There is no validation errors";
				errorMessage="";

			} else {
				message="";
				errorMessage = "There are errors\r";
				// show all the validation error
				validationResult.forEach(vm -> errorMessage += vm.getMessage() + "\r");

			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage=e.getClass().getName();
			e.printStackTrace();
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
