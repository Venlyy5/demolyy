import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * Excel读取监听器
 * @author LiYangYang
 * @date 2022/8/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelListener extends AnalysisEventListener {

    private List<Object> datalist = new ArrayList<>();

    @Override
    public void invoke(Object obj, AnalysisContext analysisContext) {
        if(obj != null){
            datalist.add(obj);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }

}