package com.yyide.chatim.activity

import android.os.Bundle
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityFaceCaptureProtocolBinding

class FaceCaptureProtocolActivity : BaseActivity() {
    lateinit var faceCaptureProtocolBinding: ActivityFaceCaptureProtocolBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        faceCaptureProtocolBinding = ActivityFaceCaptureProtocolBinding.inflate(layoutInflater)
        setContentView(faceCaptureProtocolBinding.root)
        initView()
    }

    private fun initView() {
        val content = "欢迎您使用一加壹人脸识别服务。\n" +
                "\n" +
                "在您使用一加壹人脸识别服务（以下简称“本服务”）前，您应当阅读并遵守《一加壹人脸识别服务协议》（以下简称“本协议”）。请您务必审慎阅读、充分理解各条款内容，特别是免除或者限制责任的条款。\n" +
                "\n" +
                "除非您已阅读并接受本协议所有条款，否则您无权使用本服务。您的使用行为即视为您已阅读并同意遵循本协议条款的约束及一加壹官方网站（www.1yide.com/）上的相关规范、规则和使用流程。\n" +
                "\n" +
                "一、协议的范围\n" +
                "1.1协议适用主体范围\n" +
                "本协议是您与深圳市一德文化科技有限公司（以下简称“本公司”）之间关于您使用一加壹人脸识别服务所订立的有效协议。\n" +
                "1.2协议关系及冲突条款\n" +
                "本协议被视为《一加壹用户协议》与《法律声明》的补充协议，是其不可分割的组成部分，与其构成统一整体。本协议与上述内容存在冲突的，以本协议为准。\n" +
                "本协议内容同时包括本公司可能不断发布的关于本服务的相关协议、业务规则等内容。上述内容一经正式发布，即为本协议不可分割的组成部分，您同样应当遵守。\n" +
                "\n" +
                "二、关于本服务\n" +
                "2.1本服务的内容\n" +
                "本服务内容是指本公司通过公有云平台，整合第三方技术向用户提供的一款人脸识别服务。\n" +
                "2.2 本服务的形式及许可范围\n" +
                "2.2.1 本公司给予您一项不可转让及非排他性的许可。\n" +
                "2.2.2 本条及本协议其他条款未明示授权的其他一切权利仍由本公司保留，您在行使这些权利时须另外取得本公司的书面许可。本公司如果未行使前述任何权利，并不构成对该权利的放弃。\n" +
                "\n" +
                "三、服务的使用\n" +
                "3.1您理解并同意，使用本服务是您自行独立审慎判断的结果，您将对此负责；且在使用该服务过程中，您将对自行操作的行为及其产生的结果负责，请您自行把握风险谨慎操作，并应确保。\n" +
                "3.1.1在使用本服务前，仔细阅读本公司在官方页面上展示的相关服务说明、技术规范、使用流程，并理解相关内容及可能发生的后果；\n" +
                "3.1.2在使用本服务过程中，将依照相关操作指引进行操作。\n" +
                "3.2您承诺，您在使用本服务过程中：\n" +
                "3.2.1您不应进行任何破坏或试图破坏网络安全的行为（包括但不限于钓鱼，黑客，网络诈骗，网站或空间中含有或涉嫌散播：病毒、木马、恶意代码，及通过虚拟服务器对其他网站、服务器进行涉嫌攻击行为如扫描、嗅探、ARP欺骗、DDOS等）；\n" +
                "3.2.2您不应进行任何改变或试图改变本公司提供的系统配置或破坏系统安全及网络安全的行为；\n" +
                "3.2.3您不应修改、翻译、改编、出租、转许可、在信息网络上传播或转让本公司提供的软件或服务，也不得逆向工程、反编译或试图以其他方式发现本公司提供的软件的源代码；\n" +
                "3.2.4非经本公司事先书面同意，您不应复制、传播、转让、许可或提供他人使用本公司提供的本服务；\n" +
                "3.2.5您不应以任何将会违反国家、地方法律法规、行业惯例和社会公共道德，及影响、损害或可能影响、损害本公司利益的方式或目的使用本服务；\n" +
                "3.2.6其他未经本公司明示授权的行为。\n" +
                "\n" +
                "四、数据信息安全\n" +
                "4.1 本公司将会采取合理的措施保护您的信息安全。除法律法规规定的情形外，未经您许可本公司不会向第三方公开、透露您的信息。本公司对相关信息采用专业加密存储与传输方式，保障您的信息安全;\n" +
                "4.2本公司将运用各种安全技术和程序建立完善的管理制度来保护您的信息，以免遭受未经授权的访问、使用或披露。未经您的同意，本公司不会向本公司以外的任何公司、组织和个人披露您的信息，但以下情形除外：\n" +
                "4.2.1 依据有关法律、法规应当提供的；\n" +
                "4.2.2 行政、司法机关依职权要求提供的；\n" +
                "4.2.3您同意本公司向第三方提供的；\n" +
                "4.2.4为解决举报事件、提起诉讼而需要提供的；\n" +
                "4.2.5为防止违法行为或涉嫌犯罪行为采取必要措施而需要提供的；\n" +
                "4.2.6为向您提供相关服务而必须向第三方提供的。\n" +
                "4.3您应当妥善使用和保管本服务账号及密码，并对其账号和密码下进行的行为和发生的事件负责。当您发现本服务账号被未经其授权的第三方使用或存在其他账号安全问题时应立即有效通知本公司，要求本公司暂停该账号的服务。本公司有权在合理时间内对您的该等请求采取行动，但对采取行动前您已经遭受的损失不承担任何责任。您在未经本公司同意的情况下不得将本服务账号以赠与、借用、租用、转让或其他方式处分给他人。您应对本服务中的内容自行加以判断，并承担因使用内容而引起的所有风险，包括因对内容的正确性、完整性或实用性的依赖而产生的风险；\n" +
                "4.4 您理解并同意本公司将会尽其商业上的合理努力保障您在本服务的数据存储安全，但是，本公司并不能就此提供完全保证，包括但不限于以下情形：\n" +
                "4.4.1 本公司不对您在本服务相关数据的删除或储存失败负责；\n" +
                "4.4.2 本公司有权根据实际情况自行决定单个用户在本服务数据的最长储存期限，并在服务器上为其分配数据最大存储空间等。您可根据自己的需要自行备份本服务的相关数据；\n" +
                "4..4.3 如果您停止使用本服务或服务被终止或取消，本公司可以从服务器上永久地删除您的数据。服务停止、终止或取消后，本公司没有义务向您返还任何数据。\n" +
                "\n" +
                "五、双方的权利和义务\n" +
                "5.1 您应向本公司提供真实、准确、完整的电子邮箱、手机号码、通讯地址等联系方式，以便本公司与您进行及时、有效联系。您应完全独自承担因通过这些联系方式无法与您取得联系而导致的您在使用本服务过程中遭受的任何损失。您理解并同意，您有义务保持您提供的联系方式的有效性，如有变更需要更新的，您应按本公司的要求进行操作。\n" +
                "5.2本公司根据您选购的产品和服务提供相应的服务和技术支持，您在使用过程中，如因您的原因导致故障或错误，需要本公司协助解决或调试的，本公司有权根据具体情节收取相应费用。\n" +
                "\n" +
                "六、特别声明\n" +
                "6.1 因不可抗力，使本服务条款不能履行的，遭受不可抗力的一方不承担责任。不可抗力是指本协议双方不能合理控制、不可预见或即使预见也无法避免的事件，该事件妨碍、影响或延误任何一方依据本协议履行其全部或部分义务。该类事件包括但不限于政府行为、地震、台风、洪水、火灾、其它天灾、战争或其它类似的事件。\n" +
                "6.2 鉴于计算机、互联网的特殊性，因黑客问题、电信部门技术调整、全球性网络问题、政府管制和不可抗力等引起的事件，不属于本公司违约，本公司不应承担责任。\n" +
                "6.3 您理解并同意，在免费试用期间，本公司虽然对本服务提供可用性支撑，但不对其中任何错误或漏洞提供任何担保，并不对您使用本服务的工作或结果承担任何责任。\n" +
                "6.4 本公司无法保证其所提供的服务毫无瑕疵（如本公司安全产品并不能保证您的硬件或软件的绝对安全），但本公司承诺不断提升服务质量及服务水平。您需同意：即使本公司提供的服务存在瑕疵，但上述瑕疵是当时行业技术水平所无法避免的，其将不被视为本公司违约，您同意和本公司一起合作解决上述瑕疵问题。\n" +
                "6.5您已明确知悉，人脸识别在现有技术层面仍存在局限性或不确定性，一加壹提供的识别结果仅供您参考使用，您基于人脸识别结果作出的其他决定或实施的其他行为与一加壹无关，一加壹亦不承担任何责任。\n" +
                "\n" +
                "七、第三方软件或技术\n" +
                "7.1 本服务可能会使用第三方软件或技术（包括本服务可能使用的开源代码和公共领域代码等，下同），这种使用已经获得合法授权。\n" +
                "7.2 本服务如果使用了第三方的技术，本公司将按照相关法规或约定，对相关的协议或其他文件，可能通过本协议附件、在本服务安装包特定文件夹中打包等形式进行展示，它们可能会以“服务使用许可协议”、“授权协议”、“开源代码许可证”或其他形式来表达。前述通过各种形式展现的相关协议或其他文件，均是本协议不可分割的组成部分，与本协议具有同等的法律效力，您应当遵守这些要求。如果您没有遵守这些要求，该第三方或者国家机关可能会对您提起诉讼、罚款或采取其他制裁措施，并要求本公司给予协助，您应当自行承担法律责任。\n" +
                "\n" +
                "八、违约责任\n" +
                "8.1本公司或您违反本协议的约定即构成违约，违约方应当向守约方承担违约责任。\n" +
                "8.2 如您违反本协议约定，则本公司有权终止为您提供服务，并要求您赔偿损失。\n" +
                "8.3如您以虚构事实等方式恶意诋毁本公司的商誉，本公司有权要求您向本公司公开道歉，并赔偿您给本公司造成的损失。\n" +
                "\n" +
                "九、变更和终止\n" +
                "9.1 本公司有权随时根据有关法律、法规的变化以及公司经营状况和经营策略的调整等修改本服务条款。您可以从一加壹官方网站查阅相关协议条款。本协议条款变更后，如果您继续使用本服务，即视为您已接受修改后的协议。如果您不接受修改后的协议，应当停止使用本服务。\n" +
                "9.2您理解并认可，本公司保留随时修改、取消、增强本服务一项或多项功能的权利，并有权要求您使用最新更新的版本；\n" +
                "9.3本公司保留随时终止提供本服务的权利；届时，本公司将以提前通过在网站内合适版面发布公告或发送站内通知等方式通知您；\n" +
                "9.4如因系统维护或升级的需要而暂停本服务的，本公司将尽可能事先进行通告；\n" +
                "9.5如您有任何违反本服务条款的情形，或经本公司根据自己的独立判断认为您的使用行为不符合本公司的要求，本公司除有权随时中断或终止您使用本服务而无须通知您，并将限制您新订购本服务的权限；同时，如给本公司造成损失的，本公司有权要求您赔偿损失。\n" +
                "\n" +
                "十、争议解决\n" +
                "本服务条款受中华人民共和国法律管辖。因本协议的履行发生争议的应通过友好协商解决。\n" +
                "\n" +
                "十一、附则\n" +
                "11.1 本协议自发布之日起生效。\n" +
                "11.2本协议项下所有的通知均可通过重要页面公告、电子邮件或常规的信件传送等方式进行。该等通知于发送之日视为已送达收件人。\n" +
                "11.3 如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行力，本协议的其余条款仍应有效并且有约束力。\n" +
                "\n"
        faceCaptureProtocolBinding.top.title.text = "一加壹人脸识别用户协议"
        faceCaptureProtocolBinding.top.backLayout.setOnClickListener {
            finish()
        }
        faceCaptureProtocolBinding.tvContent.text = content
    }

    override fun getContentViewID(): Int = R.layout.activity_face_capture_protocol
}