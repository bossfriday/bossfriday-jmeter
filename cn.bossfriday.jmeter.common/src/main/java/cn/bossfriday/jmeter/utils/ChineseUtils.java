package cn.bossfriday.jmeter.utils;

import cn.bossfriday.jmeter.common.Combo2;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * ChineseUtils
 *
 * @author chenx
 * @date 2022/09/06
 */
public class ChineseUtils {

    private static List<String> departmentList = new ArrayList<>();
    private static List<String> groupList = new ArrayList<>();
    private static Random random = new Random();

    /**
     * 姓氏资源
     */
    private static final String[] LAST_NAMES = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
            "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
            "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷",
            "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和",
            "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
            "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童", "颜", "郭", "梅", "盛", "林", "刁", "钟",
            "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "应",
            "宗", "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁", "荀",
            "羊", "于", "惠", "甄", "曲", "家", "封", "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓", "牧", "隗", "山",
            "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫", "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武", "符", "刘", "景",
            "詹", "束", "龙", "叶", "幸", "司", "韶", "郜", "黎", "蓟", "溥", "印", "宿", "白", "怀", "蒲", "邰", "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠",
            "蒙", "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍", "却",
            "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "浦", "尚", "农", "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹", "习",
            "宦", "艾", "鱼", "容", "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘", "匡", "国", "文", "寇", "广", "禄",
            "阙", "东", "欧", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶", "空",
            "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红", "游", "郏", "竺", "权", "逯", "盖", "益", "桓", "公", "仉",
            "督", "岳", "帅", "缑", "亢", "况", "郈", "有", "琴", "归", "海", "晋", "楚", "闫", "法", "汝", "鄢", "涂", "钦", "商", "牟", "佘", "佴", "伯", "赏", "墨",
            "哈", "谯", "篁", "年", "爱", "阳", "佟", "言", "福", "南", "火", "铁", "迟", "漆", "官", "冼", "真", "展", "繁", "檀", "祭", "密", "敬", "揭", "舜", "楼",
            "疏", "冒", "浑", "挚", "胶", "随", "高", "皋", "原", "种", "练", "弥", "仓", "眭", "蹇", "覃", "阿", "门", "恽", "来", "綦", "召", "仪", "风", "介", "巨",
            "木", "京", "狐", "郇", "虎", "枚", "抗", "达", "杞", "苌", "折", "麦", "庆", "过", "竹", "端", "鲜", "皇", "亓", "老", "是", "秘", "畅", "邝", "还", "宾",
            "闾", "辜", "纵", "侴", "万俟", "司马", "上官", "欧阳", "夏侯", "诸葛", "闻人", "东方", "赫连", "皇甫", "羊舌", "尉迟", "公羊", "澹台", "公冶", "宗正",
            "濮阳", "淳于", "单于", "太叔", "申屠", "公孙", "仲孙", "轩辕", "令狐", "钟离", "宇文", "长孙", "慕容", "鲜于", "闾丘", "司徒", "司空", "兀官", "司寇",
            "南门", "呼延", "子车", "颛孙", "端木", "巫马", "公西", "漆雕", "车正", "壤驷", "公良", "拓跋", "夹谷", "宰父", "谷梁", "段干", "百里", "东郭", "微生",
            "梁丘", "左丘", "东门", "西门", "南宫", "第五", "公仪", "公乘", "太史", "仲长", "叔孙", "屈突", "尔朱", "东乡", "相里", "胡母", "司城", "张廖", "雍门",
            "毋丘", "贺兰", "綦毋", "屋庐", "独孤", "南郭", "北宫", "王孙"};

    /**
     * 地区资源
     */
    private static final String[] AREAS = {"北京", "上海", "天津", "重庆", "黑龙江", "吉林", "辽宁", "山东", "山西", "陕西", "河北", "河南", "湖北", "湖南", "海南", "江苏", "江西",
            "广东", "广西", "云南", "贵州", "四川", "内蒙古", "宁夏", "甘肃", "青海", "安徽", "浙江", "福建", "西藏", "台湾", "香港", "澳门", "东城区", "黄浦区", "和平区", "万州区", "哈尔滨市",
            "长春市", "沈阳市", "济南市", "太原市", "西安市", "石家庄市", "郑州市", "武汉市", "长沙市", "海口市", "南京市", "南昌市", "广州市", "南宁市", "昆明市", "贵阳市", "成都市", "呼和浩特市",
            "银川市", "兰州市", "西宁市", "合肥市", "杭州市", "福州市", "拉萨市", "台北市", "中西区", "西城区", "卢湾区", "河东区", "涪陵区", "齐齐哈尔市", "吉林市", "大连市", "青岛市",
            "大同市", "铜川市", "唐山市", "开封市", "黄石市", "株洲市", "三亚市", "无锡市", "景德镇市", "韶关市", "柳州市", "曲靖市", "六盘水市", "自贡市", "包头市", "石嘴山市", "嘉峪关市",
            "海东地区", "芜湖市", "宁波市", "厦门市", "昌都地区", "高雄市", "湾仔区", "崇文区", "徐汇区", "河西区", "渝中区", "鸡西市", "四平市", "鞍山市", "淄博市", "阳泉市", "宝鸡市",
            "秦皇岛市", "洛阳市", "十堰市", "湘潭市", "五指山市", "徐州市", "萍乡市", "深圳市", "桂林市", "玉溪市", "遵义市", "攀枝花市", "乌海市", "吴忠市", "金昌市", "海北藏族自治州",
            "蚌埠市", "温州市", "莆田市", "山南地区新疆", "基隆市", "东区", "宣武区", "长宁区", "南开区", "大渡口区", "鹤岗市", "辽源市", "抚顺市", "枣庄市", "长治市", "咸阳市", "邯郸市",
            "平顶山市", "宜昌市", "衡阳市", "琼海市", "常州市", "九江市", "珠海市", "梧州市", "保山市", "安顺市", "泸州市", "赤峰市", "固原市", "白银市", "黄南藏族自治州", "淮南市", "嘉兴市",
            "三明市", "乌鲁木齐市", "台中市", "南区", "朝阳区", "静安区", "河北区", "江北区", "双鸭山市", "通化市", "本溪市", "东营市", "晋城市", "渭南市", "邢台市", "安阳市", "襄樊市", "邵阳市",
            "儋州市", "苏州市", "新余市", "汕头市", "北海市", "昭通市", "铜仁地区", "德阳市", "通辽市", "中卫市", "天水市", "海南藏族自治州", "马鞍山市", "湖州市", "泉州市", "克拉玛依市", "台南市",
            "油尖旺区", "丰台区", "普陀区", "红桥区", "沙坪坝区", "大庆市", "白山市", "丹东市", "烟台市", "朔州市", "延安市", "保定市", "鹤壁市", "鄂州市", "岳阳市", "文昌市", "南通市", "鹰潭市",
            "佛山市", "防城港市", "丽江市", "黔西南布依族苗族自治州", "绵阳市", "鄂尔多斯市", "武威市", "果洛藏族自治州", "淮北市", "绍兴市", "漳州市", "吐鲁番地区", "新竹市", "深水埗区",
            "石景山区", "闸北区", "塘沽区", "九龙坡区", "伊春市", "松原市", "锦州市", "潍坊市", "晋中市", "汉中市", "张家口市", "新乡市", "荆门市", "常德市", "万宁市", "连云港市", "赣州市",
            "江门市", "钦州市", "思茅市", "毕节地区", "广元市", "呼伦贝尔市", "张掖市", "玉树藏族自治州", "铜陵市", "金华市", "南平市", "哈密地区日喀则地区", "嘉义市", "九龙城区", "海淀区",
            "虹口区", "汉沽区", "南岸区", "佳木斯市", "白城市", "营口市", "济宁市", "运城市", "榆林市", "承德市", "焦作市", "孝感市", "张家界市", "东方市", "淮安市", "吉安市", "湛江市",
            "贵港市", "临沧市", "黔东南苗族侗族自治州", "遂宁市", "巴彦淖尔市", "平凉市", "海西蒙古族藏族自治州", "安庆市", "衢州市", "龙岩市", "那曲地区", "黄大仙区", "门头沟区", "杨浦区",
            "大港区", "北碚区", "七台河市", "延边朝鲜族自治州", "阜新市", "泰安市", "忻州市", "安康市", "沧州市", "济源市", "荆州市", "益阳市", "定安县", "盐城市", "宜春市", "茂名市", "玉林市",
            "楚雄彝族自治州", "黔南布依族苗族自治州", "内江市", "乌兰察布市", "酒泉市", "黄山市", "舟山市", "宁德市", "阿里地区", "观塘区", "房山区", "闵行区", "东丽区", "万盛区", "牡丹江市",
            "辽阳市", "威海市", "临汾市", "商洛市", "廊坊市", "濮阳市", "黄冈市", "郴州市", "屯昌县", "扬州市", "抚州市", "肇庆市", "百色市", "红河哈尼族彝族自治州", "乐山市", "兴安盟",
            "庆阳市", "滁州市", "台州市", "林芝地区", "荃湾区", "通州区", "宝山区", "西青区", "双桥区", "黑河市", "盘锦市", "日照市", "吕梁市", "衡水市", "许昌市", "咸宁市", "永州市", "澄迈县",
            "镇江市", "上饶市", "惠州市", "贺州市", "文山壮族苗族自治州", "南充市", "锡林郭勒盟", "定西市", "阜阳市", "丽水市", "昌吉回族自治州", "葵青区", "顺义区", "嘉定区", "津南区", "渝北区",
            "绥化市", "铁岭市", "莱芜市", "漯河市", "随州市", "怀化市", "临高县", "泰州市", "梅州市", "河池市", "西双版纳傣族自治州", "眉山市", "阿拉善盟", "陇南市", "宿州市",
            "博尔塔拉蒙古自治州", "沙田区", "昌平区", "浦东新区", "北辰区", "巴南区", "大兴安岭地区", "朝阳市", "临沂市", "三门峡市", "恩施土家族苗族自治州", "娄底市", "白沙黎族自治县",
            "宿迁市", "汕尾市", "来宾市", "大理白族自治州", "宜宾市", "临夏回族自治州", "巢湖市", "巴音郭楞蒙古自治州", "西贡区", "大兴区", "金山区", "武清区", "黔江区", "葫芦岛市", "德州市",
            "南阳市", "仙桃市", "湘西土家族苗族自治州", "昌江黎族自治县", "河源市", "崇左市", "德宏傣族景颇族自治州", "广安市", "甘南藏族自治州", "六安市", "阿克苏地区", "大埔区", "怀柔区",
            "松江区", "宝坻区", "长寿区", "聊城市", "商丘市", "潜江市", "乐东黎族自治县", "阳江市", "怒江傈僳族自治州", "达州市", "亳州市", "克孜勒苏柯尔克孜自治州", "北区", "平谷区", "青浦区",
            "宁河县", "江津区", "滨州市", "信阳市", "天门市", "陵水黎族自治县", "清远市", "迪庆藏族自治州", "雅安市", "池州市", "喀什地区", "元朗区", "密云县", "南汇区", "静海县", "合川区",
            "菏泽市", "周口市", "神农架林区", "保亭黎族苗族自治县", "东莞市", "巴中市", "宣城市", "和田地区", "屯门区", "延庆县", "奉贤区", "蓟县", "永川区", "驻马店市", "琼中黎族苗族自治县",
            "中山市", "资阳市", "伊犁哈萨克自治州", "离岛区", "崇明县", "南川区", "潮州市", "阿坝藏族羌族自治州", "塔城地区", "綦江县", "揭阳市", "甘孜藏族自治州", "阿勒泰地区", "潼南县",
            "云浮市", "凉山彝族自治州", "石河子市", "铜梁县", "阿拉尔市", "大足县", "图木舒克市", "荣昌县", "五家渠市", "璧山县", "梁平县", "城口县", "丰都县", "垫江县", "武隆县"};

    /**
     * 职能资源
     */
    private static final List<String> DUTIES = Arrays.asList("研发", "市场", "财务", "法务", "销售", "人力", "管理", "行政", "测试", "运维");

    /**
     * 部门资源
     */
    private static final List<String> DEPARTMENTS = Arrays.asList("1部", "2部", "3部", "4部", "5部", "6部", "7部", "8部", "9部", "10部", "11部", "12部", "13部", "14部", "15部", "16部", "17部", "18部", "19部", "20部");

    /**
     * 群组资源
     */
    private static final List<String> GROUPS = Arrays.asList("1群", "2群", "3群", "4群", "5群", "6群", "7群", "8群", "9群", "10群", "11群", "12群", "13群", "14群", "15群", "16群", "17群", "18群", "19群", "20群");

    private static final String CHAR_SET = "GB2312";

    static {
        for (String par1 : AREAS) {
            for (String part2 : DUTIES) {
                for (String par3 : DEPARTMENTS) {
                    departmentList.add(par1 + part2 + par3);
                }
            }
        }

        for (String par1 : AREAS) {
            for (String part2 : DUTIES) {
                for (String par3 : GROUPS) {
                    groupList.add(par1 + part2 + par3);
                }
            }
        }
    }

    private ChineseUtils() {

    }

    /**
     * getRandomChinese（获取随机中文）
     *
     * @return
     * @throws Exception
     */
    public static String getRandomChinese() throws UnsupportedEncodingException {
        int highPos = (176 + Math.abs(random.nextInt(71)));
        random = new Random();
        int lowPos = 161 + Math.abs(random.nextInt(94));

        byte[] bArr = new byte[2];
        bArr[0] = (Integer.valueOf(highPos)).byteValue();
        bArr[1] = (Integer.valueOf(lowPos)).byteValue();

        return new String(bArr, CHAR_SET);
    }

    /**
     * getRandomLastChineseName（获取随机姓氏）
     *
     * @return
     */
    public static String getRandomLastChineseName() {
        return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    /**
     * getRandomChineseName（获取随机中文名字）
     *
     * @param firstNameLength
     * @return V1:lastName, V2:firstName
     * @throws UnsupportedEncodingException
     */
    public static Combo2<String, String> getRandomChineseName(int firstNameLength) throws UnsupportedEncodingException {
        if (firstNameLength <= 0) {
            firstNameLength = 1;
        }

        String lastName = getRandomLastChineseName();
        StringBuilder firstNameBuilder = new StringBuilder();
        for (int i = 0; i < firstNameLength; i++) {
            firstNameBuilder.append(getRandomChinese());
        }

        return new Combo2<>(lastName, firstNameBuilder.toString());
    }

    /**
     * getDepartmentName
     *
     * @param index
     * @return
     */
    public static String getDepartmentName(int index) {
        if (index < 0) {
            index = 0;
        }

        String result = departmentList.get(index % departmentList.size());
        if (index > departmentList.size()) {
            result = result + index;
        }

        return result;
    }

    /**
     * getGroupName
     *
     * @param index
     * @return
     */
    public static String getGroupName(int index) {
        if (index < 0) {
            index = 0;
        }

        String result = groupList.get(index % groupList.size());
        if (index > groupList.size()) {
            result = result + index;
        }

        return result;
    }
}
