import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yyide.chatim_pro.kotlin.network.NetworkApi
import com.yyide.chatim_pro.model.NewAppRspJ
import kotlinx.coroutines.launch

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/7 16:12
 * @Description :新控制台的请求
 */
class NewAppViewModel : ViewModel() {
    val nAppList = MutableLiveData<Result<MutableList<NewAppRspJ>>>()

    /**
     * 获取控制台App列表
     */
    fun getApplist() {
        viewModelScope.launch {
            val result = NetworkApi.getAppList()
            nAppList.value = result
        }
    }

}