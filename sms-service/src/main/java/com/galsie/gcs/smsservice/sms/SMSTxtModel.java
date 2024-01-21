package com.galsie.gcs.smsservice.sms;

import com.galsie.lib.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class SMSTxtModel {
    private static final String BASE_PATH = "txt";
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("%.*%");
    private String txtText;
    private List<String> variables;


    public String getReplacedTXT(Map<String, String> variableReplacement){
        String txtText = this.txtText;
        for (String var: variableReplacement.keySet()){
            txtText = txtText.replaceAll("%" + var + "%", variableReplacement.get(var));
        }
        return txtText;
    }


    /**
     *
     * @param filePath the path in HtmlEmail.BASE_PATH
     * @return
     * @throws NullPointerException if file couldn't open
     */
    public static SMSTxtModel fromTxtFile(String filePath) throws NullPointerException, IOException {
        var resource = new ClassPathResource(StringUtils.joinPaths(BASE_PATH, filePath));
        var data = resource.getInputStream().readAllBytes();
        return SMSTxtModel.fromTxtText(new String(data, StandardCharsets.UTF_8));
    }

    public static SMSTxtModel fromTxtText(String txtText){
        List<String> variables = VARIABLE_PATTERN.matcher(txtText).results().map(MatchResult::group).map((dt) -> dt.replaceAll("%", "")).collect(Collectors.toList());
        return new SMSTxtModel(txtText, variables);
    }
}
