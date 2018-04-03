package com.lijinshan.jsonview;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvJson;
    private JsonAdapter jsonAdapter;
    private Button btnExpand, btnCollapse, btnGenerateJson, btnShowDialog;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvJson = findViewById(R.id.rvJson);
        btnExpand = findViewById(R.id.btnExpand);
        btnCollapse = findViewById(R.id.btnCollapse);
        btnGenerateJson = findViewById(R.id.btnGenerateJson);
        btnShowDialog = findViewById(R.id.btnShowDialog);
        rvJson.setLayoutManager(new LinearLayoutManager(this));
        jsonAdapter = new JsonAdapter();
        jsonAdapter.setJsonData("{\n" +
                "\"code\": 100,\n" +
                "\"msg\": \"首页获取成功\",\n" +
                "\"data\": {\n" +
                "\"shareData\": null,\n" +
                "\"pageInfo\": {\n" +
                "\"pageNumber\": 1,\n" +
                "\"totalItem\": 1,\n" +
                "\"totalPage\": 1,\n" +
                "\"pageSize\": 14\n" +
                "},\n" +
                "\"itemList\": [\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"itemType\": 4,\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 0,\n" +
                "\"showListButtonTitle\": \"\"\n" +
                "},\n" +
                "\"addTime\": 1485400281000,\n" +
                "\"imagePath\": \"\",\n" +
                "\"weight\": 0,\n" +
                "\"title\": \"新版首页9\",\n" +
                "\"circleImgRadius\": 0,\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 400,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_0\",\n" +
                "\"titleZh\": \"新版首页9\",\n" +
                "\"orderTime\": 1516936061000,\n" +
                "\"titleEn\": \"test9\",\n" +
                "\"isTop\": 2,\n" +
                "\"showTitle\": 1,\n" +
                "\"subtitle\": \"新版首页9副标题\",\n" +
                "\"coverWidth\": 400,\n" +
                "\"circleImg\": 0,\n" +
                "\"id\": 1217356,\n" +
                "\"collectionItems\": [\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net/http://yi23imgtest.oss-cn-beijing.aliyuncs.com/Thumbs/216-20180223_184613-1519382773295-1.jpg!300w\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 1,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=54\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217356\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net/http://yi23imgtest.oss-cn-beijing.aliyuncs.com/Thumbs/216-20180223_184505-1519382705251-1.jpg!300w\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 2,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=54\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217356\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net/http://yi23imgtest.oss-cn-beijing.aliyuncs.com/Thumbs/41-20180208_125803-1518065883903-1.jpg!300w\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 3,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=54\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217356\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net/http://yi23imgtest.oss-cn-beijing.aliyuncs.com/Thumbs/41-20180208_125905-1518065945943-1.jpg!300w\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 4,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=54\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217356\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net/http://yi23imgtest.oss-cn-beijing.aliyuncs.com/Thumbs/68-20180207_134356-1517982236085-1.jpg!300w\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 5,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=54\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217356\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net/http://yi23imgtest.oss-cn-beijing.aliyuncs.com/Thumbs/41-20180131_135254-1517377974640-1.jpg!300w\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 6,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=54\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217356\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net/http://yi23imgtest.oss-cn-beijing.aliyuncs.com/Thumbs/41-20180206_104740-1517885260546-1.jpg!300w\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 7,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=54\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217356\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net/http://yi23imgtest.oss-cn-beijing.aliyuncs.com/Thumbs/41-20180131_142605-1517379965092-1.jpg!300w\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 8,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=54\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217356\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net/http://yi23imgtest.oss-cn-beijing.aliyuncs.com/Thumbs/41-20180208_111410-1518059650994-1.jpg!300w\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 9,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"https://www.95vintage.com/yi23/Home/Subscribe/promotionListPage?key=韩范潮流,Sugaricing全场49元起&jumpNativeType=33&jumpNativeId=韩范潮流,Sugaricing全场49元起\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217356\n" +
                "}\n" +
                "],\n" +
                "\"editorAddTime\": 1516936061000\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"itemType\": 4,\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 0,\n" +
                "\"showListButtonTitle\": \"\"\n" +
                "},\n" +
                "\"addTime\": 1485400280000,\n" +
                "\"imagePath\": \"\",\n" +
                "\"weight\": 0,\n" +
                "\"title\": \"新版首页6\",\n" +
                "\"circleImgRadius\": 0,\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 400,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_1\",\n" +
                "\"titleZh\": \"新版首页6\",\n" +
                "\"orderTime\": 1516936061000,\n" +
                "\"titleEn\": \"test6\",\n" +
                "\"isTop\": 2,\n" +
                "\"showTitle\": 1,\n" +
                "\"subtitle\": \"新版首页6副标题\",\n" +
                "\"coverWidth\": 400,\n" +
                "\"circleImg\": 0,\n" +
                "\"id\": 1217357,\n" +
                "\"collectionItems\": [\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net//collections/14-20180126_160209-1516953729248-1.jpg\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 1,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"https://www.95vintage.com/yi23/Home/Subscribe/promotionListPage?key=Top100,1%E6%9C%88%E6%9C%80%E5%8F%97%E6%AC%A2%E8%BF%8E%E6%A6%9C%E5%8D%95&jumpNativeType=33&jumpNativeId=Top100,1%E6%9C%88%E6%9C%80%E5%8F%97%E6%AC%A2%E8%BF%8E%E6%A6%9C%E5%8D%95\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217357\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net//collections/14-20180126_160217-1516953737435-1.jpg\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 2,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"ttp://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=31&jumpNativeId=600\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217357\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net//collections/14-20180126_160225-1516953745412-1.jpg\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 3,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"ttp://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=31&jumpNativeId=600\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217357\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net//collections/14-20180126_160233-1516953753835-1.jpg\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 4,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"ttp://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=31&jumpNativeId=600\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217357\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net//collections/14-20180126_160244-1516953764201-1.jpg\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 5,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"ttp://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=31&jumpNativeId=600\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217357\n" +
                "},\n" +
                "{\n" +
                "\"brandName\": \"\",\n" +
                "\"productId\": 0,\n" +
                "\"imagePath\": \"http://tu.yi23.net//collections/14-20180126_160256-1516953776770-1.jpg\",\n" +
                "\"searchKey\": \"\",\n" +
                "\"sort\": 6,\n" +
                "\"tagText\": \"\",\n" +
                "\"productName\": \"\",\n" +
                "\"url\": \"ttp://www.95vintage.com/yi23/Home/Index/postingPage?id=7037&jumpNativeType=31&jumpNativeId=600\",\n" +
                "\"itemText\": \"\",\n" +
                "\"path\": \"\",\n" +
                "\"itemSubText\": \"\",\n" +
                "\"stockNum\": 0,\n" +
                "\"tagType\": 0,\n" +
                "\"collectionId\": 1217357\n" +
                "}\n" +
                "],\n" +
                "\"editorAddTime\": 1516936061000\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1517458897000,\n" +
                "\"itemType\": 3,\n" +
                "\"editorialTitle\": \"\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 0,\n" +
                "\"showListButtonTitle\": \"\"\n" +
                "},\n" +
                "\"addTime\": 1517804713000,\n" +
                "\"imagePath\": \"http://yi23imgtest.oss-cn-beijing.aliyuncs.com/posting/95-20180205_122543-1517804743760-1.jpg!750w\",\n" +
                "\"editorialThumbnail\": \"\",\n" +
                "\"weight\": 583,\n" +
                "\"title\": \"复古系时髦单品推荐\",\n" +
                "\"url\": \"http://testwx.95vintage.com/yi23/Home/Index/index\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 360,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_6\",\n" +
                "\"titleZh\": \"复古系时髦单品推荐\",\n" +
                "\"titleEn\": \"回到1990年\",\n" +
                "\"isTop\": 1,\n" +
                "\"showTitle\": 1,\n" +
                "\"coverWidth\": 750,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1515312147000,\n" +
                "\"itemType\": 3,\n" +
                "\"editorialTitle\": \"\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 1,\n" +
                "\"showListButtonTitle\": \"\"\n" +
                "},\n" +
                "\"addTime\": 1516176262000,\n" +
                "\"imagePath\": \"!750w\",\n" +
                "\"editorialThumbnail\": \"\",\n" +
                "\"weight\": -1,\n" +
                "\"title\": \"全球大牌无限换穿\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=6863&jumpNativeType=20&jumpNativeId=6863\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 1,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_11\",\n" +
                "\"titleZh\": \"全球大牌无限换穿\",\n" +
                "\"titleEn\": \"WAOU LANDING\",\n" +
                "\"isTop\": 1,\n" +
                "\"showTitle\": 1,\n" +
                "\"coverWidth\": 1,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1510588800000,\n" +
                "\"itemType\": 3,\n" +
                "\"editorialTitle\": \"\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 1,\n" +
                "\"bottomLine\": 1,\n" +
                "\"showListButtonTitle\": \"\"\n" +
                "},\n" +
                "\"addTime\": 1510543068000,\n" +
                "\"imagePath\": \"http://tu.yi23.net/posting/118-20171114_105928-1510628368688.png!750w\",\n" +
                "\"editorialThumbnail\": \"\",\n" +
                "\"weight\": 999,\n" +
                "\"title\": \"双十一购衣狂欢\",\n" +
                "\"url\": \"https://mclient.alipay.com/home/exterfaceAssign.htm?subject=%E5%8A%A0%E8%A1%A3%E5%88%B8%E5%85%91%E6%8D%A2&_input_charset=utf-8&sign=D5KhNP18khDJs1KRSZyiptifXEnexpkeBGJzdx%2BwDOindIZPDGqdPQdRslVyQ1jV%2Fb8kVxP8k8uBbMcFWgMnJ1mgdB%2BJB1K9gYxLmhET7FLxyZhoYh7QKOuHXEOQoY3EptSfyq%2B4QYlH%2BpG5hHXsMVRP1UydnOUekSHNAAiwQG0%3D&notify_url=http%3A%2F%2Ftestapi.95vintage.com%2Fgw%2Fpay%2FaliCallback&alipay_exterface_invoke_assign_model=cashier&alipay_exterface_invoke_assign_target=mapi_direct_trade.htm&payment_type=1&out_trade_no=1517414277753222&partner=2088911145361708&alipay_exterface_invoke_assign_sign=_e6me_b_q_d6_odt_j3_b_wh_g_l_kn_nvn_u_qxd_s_w_i_ah12_d_n_w_sxxs_s_phhe_y_b_muvx_g_a%3D%3D&service=alipay.wap.create.direct.pay.by.user&total_fee=1.00&return_url=http%3A%2F%2Ftestwx.95vintage.com%2Fyi23%2FHome%2FBuy%2FpaySuccess&sign_type=RSA&seller_id=2088911145361708&show_url=http%3A%2F%2Ftestwx.95vintage.com%2Fyi23%2FHome%2FIndex%2Findex&alipay_exterface_invoke_assign_client_ip=101.201.227.120\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 660,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_5\",\n" +
                "\"titleZh\": \"双十一购衣狂欢\",\n" +
                "\"titleEn\": \"shopping\",\n" +
                "\"isTop\": 1,\n" +
                "\"showTitle\": 0,\n" +
                "\"coverWidth\": 1125,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1510223594000,\n" +
                "\"itemType\": 9,\n" +
                "\"editorialTitle\": \"Ayawawa：教你做一个“会zuo“的女生\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 1,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"9\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 0,\n" +
                "\"showListButtonTitle\": \"全部专题\"\n" +
                "},\n" +
                "\"addTime\": 1510223731000,\n" +
                "\"imagePath\": \"http://yi23imgtest.oss-cn-beijing.aliyuncs.com/posting/14-20180126_161158-1516954318313-1.jpg\",\n" +
                "\"editorialThumbnail\": \"http://yi23imgtest.oss-cn-beijing.aliyuncs.com/posting/14-20180126_161158-1516954318313-1.jpg\",\n" +
                "\"weight\": 9999,\n" +
                "\"title\": \"Ayawawa：教你做一个“会zuo“的女生\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=6833&jumpNativeType=20&jumpNativeId=6833\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 360,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_3\",\n" +
                "\"titleZh\": \"Ayawawa：教你做一个“会zuo“的女生\",\n" +
                "\"titleEn\": \"Ayawawa\",\n" +
                "\"isTop\": 1,\n" +
                "\"readingCount\": \"1015\",\n" +
                "\"showTitle\": 1,\n" +
                "\"subtitle\": \"积极地“作”不是无理取闹，而是不停地去发现更好的自己。\",\n" +
                "\"coverWidth\": 360,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"积极地“作”不是无理取闹，而是不停地去发现更好的自己。\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1503991380000,\n" +
                "\"itemType\": 3,\n" +
                "\"editorialTitle\": \"\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 0,\n" +
                "\"showListButtonTitle\": \"\"\n" +
                "},\n" +
                "\"addTime\": 1503991487000,\n" +
                "\"imagePath\": \"http://tu.yi23.net/yi23img/postings/cover/0d526ec4792b43ffa1923a503f87881c.jpg!750w\",\n" +
                "\"editorialThumbnail\": \"\",\n" +
                "\"weight\": 10000,\n" +
                "\"title\": \"从Prada旗袍到Gucci绢扇，时尚界刮起中国风！\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=6633&jumpNativeType=20&jumpNativeId=6633\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 360,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_2\",\n" +
                "\"titleZh\": \"从Prada旗袍到Gucci绢扇，时尚界刮起中国风！\",\n" +
                "\"titleEn\": \"Oriental Chic\",\n" +
                "\"isTop\": 1,\n" +
                "\"showTitle\": 0,\n" +
                "\"coverWidth\": 750,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1503458520000,\n" +
                "\"itemType\": 3,\n" +
                "\"editorialTitle\": \"\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 1,\n" +
                "\"showListButtonTitle\": \"\"\n" +
                "},\n" +
                "\"addTime\": 1503458984000,\n" +
                "\"imagePath\": \"http://tu.yi23.net/yi23img/postings/cover/8a12575433a2446da1b5190415710da9.jpg!750w\",\n" +
                "\"editorialThumbnail\": \"\",\n" +
                "\"weight\": 5000,\n" +
                "\"title\": \"人生如戏，那些因戏结缘的CP现在呢？\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=6615&jumpNativeType=20&jumpNativeId=6615\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 240,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_4\",\n" +
                "\"titleZh\": \"人生如戏，那些因戏结缘的CP现在呢？\",\n" +
                "\"titleEn\": \"LOVE TRANSCEND\",\n" +
                "\"isTop\": 1,\n" +
                "\"showTitle\": 0,\n" +
                "\"coverWidth\": 240,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1501473840000,\n" +
                "\"itemType\": 9,\n" +
                "\"editorialTitle\": \"嘻哈潮流跟不跟，反正可以让你看起来年轻10岁\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 1,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"http://www.95vintage.com/yi23/Home?jumpNativeType=36\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 0,\n" +
                "\"showListButtonTitle\": \"全部专题\"\n" +
                "},\n" +
                "\"addTime\": 1501474022000,\n" +
                "\"imagePath\": \"http://tu.yi23.net/yi23img/postings/cover/9d51c876661a4621a8d49bc1e17148c1.jpg\",\n" +
                "\"editorialThumbnail\": \"http://tu.yi23.net/yi23img/postings/cover/9d51c876661a4621a8d49bc1e17148c1.jpg\",\n" +
                "\"weight\": 550,\n" +
                "\"title\": \"嘻哈潮流跟不跟，反正可以让你看起来年轻10岁\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=6535\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 240,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_7\",\n" +
                "\"titleZh\": \"嘻哈潮流跟不跟，反正可以让你看起来年轻10岁\",\n" +
                "\"titleEn\": \"HIPHOP IS TRENDING NOW\",\n" +
                "\"isTop\": 1,\n" +
                "\"readingCount\": \"1220\",\n" +
                "\"showTitle\": 1,\n" +
                "\"subtitle\": \"\\\"你是什么货色\\n\",\n" +
                "\"coverWidth\": 240,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"\\\"你是什么货色\\n\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1497685020000,\n" +
                "\"itemType\": 8,\n" +
                "\"editorialTitle\": \"\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 0,\n" +
                "\"showListButtonTitle\": \"\"\n" +
                "},\n" +
                "\"addTime\": 1497685161000,\n" +
                "\"imagePath\": \"http://tu.yi23.net/yi23img/postings/cover/100b07a2c25e4f8e8ce46019cb29644e.jpg!750w\",\n" +
                "\"editorialThumbnail\": \"\",\n" +
                "\"weight\": 92,\n" +
                "\"title\": \"一人千面 每一面我都要美成佐佐木希\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 240,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_8\",\n" +
                "\"titleZh\": \"一人千面 每一面我都要美成佐佐木希\",\n" +
                "\"titleEn\": \"Practical collocation of  Japanese womens clothes\",\n" +
                "\"isTop\": 1,\n" +
                "\"showTitle\": 0,\n" +
                "\"coverWidth\": 240,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1497236580000,\n" +
                "\"itemType\": 8,\n" +
                "\"editorialTitle\": \"\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 1,\n" +
                "\"showListButtonTitle\": \"\"\n" +
                "},\n" +
                "\"addTime\": 1497236882000,\n" +
                "\"imagePath\": \"http://tu.yi23.net/yi23img/postings/cover/c657911430544122bffcedefac3da0b1.png!750w\",\n" +
                "\"editorialThumbnail\": \"\",\n" +
                "\"weight\": 90,\n" +
                "\"title\": \"造型计，如何把简单穿成有型\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 240,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_9\",\n" +
                "\"titleZh\": \"造型计，如何把简单穿成有型\",\n" +
                "\"titleEn\": \"Dress down with the best style\",\n" +
                "\"isTop\": 1,\n" +
                "\"showTitle\": 0,\n" +
                "\"coverWidth\": 240,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1487642400000,\n" +
                "\"itemType\": 3,\n" +
                "\"editorialTitle\": \"\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 0,\n" +
                "\"showListButtonTitle\": \"\"\n" +
                "},\n" +
                "\"addTime\": 1483092244000,\n" +
                "\"imagePath\": \"http://tu.yi23.net/yi23img/postings/cover/f2f543403cff46b0865edc5a717f112e.jpg!750w\",\n" +
                "\"editorialThumbnail\": \"\",\n" +
                "\"weight\": -42,\n" +
                "\"title\": \"邀请好友\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Event/shareForNew\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 360,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_12\",\n" +
                "\"titleZh\": \"邀请好友\",\n" +
                "\"titleEn\": \"yq\",\n" +
                "\"isTop\": 1,\n" +
                "\"showTitle\": 0,\n" +
                "\"coverWidth\": 750,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"editorialAddTime\": 1510136907000,\n" +
                "\"itemType\": 14,\n" +
                "\"editorialTitle\": \"咪蒙：好的衣品，也是一种职场实力\",\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 1,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"http://www.95vintage.com/yi23/Home?jumpNativeType=36\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 0,\n" +
                "\"showListButtonTitle\": \"全部商品\"\n" +
                "},\n" +
                "\"addTime\": 1478600977000,\n" +
                "\"imagePath\": \"http://yi23imgtest.oss-cn-beijing.aliyuncs.com/posting/14-20180126_161223-1516954343717-1.jpg\",\n" +
                "\"editorialThumbnail\": \"http://yi23imgtest.oss-cn-beijing.aliyuncs.com/posting/14-20180126_161223-1516954343717-1.jpg\",\n" +
                "\"weight\": 50,\n" +
                "\"title\": \"咪蒙：好的衣品，也是一种职场实力\",\n" +
                "\"url\": \"http://www.95vintage.com/yi23/Home/Index/postingPage?id=6827&jumpNativeType=20&jumpNativeId=6827\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 360,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_10\",\n" +
                "\"titleZh\": \"咪蒙：好的衣品，也是一种职场实力\",\n" +
                "\"titleEn\": \"Mimeng\",\n" +
                "\"isTop\": 1,\n" +
                "\"showTitle\": 1,\n" +
                "\"subtitle\": \"你穿得干练、职业化，\",\n" +
                "\"coverWidth\": 750,\n" +
                "\"id\": 0,\n" +
                "\"editorialText\": \"你穿得干练、职业化，\"\n" +
                "},\n" +
                "{\n" +
                "\"reason\": \"\",\n" +
                "\"itemType\": 10,\n" +
                "\"renderInfo\": {\n" +
                "\"enableShowListButton\": 0,\n" +
                "\"topline\": 0,\n" +
                "\"countdownSeconds\": 0,\n" +
                "\"showListButtonUrl\": \"\",\n" +
                "\"enableCountdown\": 0,\n" +
                "\"bottomLine\": 0,\n" +
                "\"showListButtonTitle\": \"查看全部\"\n" +
                "},\n" +
                "\"addTime\": 1521025299000,\n" +
                "\"weight\": 5000,\n" +
                "\"title\": \"礼服测试\",\n" +
                "\"itemText\": \"\",\n" +
                "\"coverHeight\": 0,\n" +
                "\"path\": \"HOME_PAGE_1_ITEMPOSITION_13\",\n" +
                "\"titleZh\": \"礼服测试\",\n" +
                "\"titleEn\": \"lifu\",\n" +
                "\"isTop\": 0,\n" +
                "\"showTitle\": 0,\n" +
                "\"subtitle\": \"\",\n" +
                "\"coverWidth\": 0,\n" +
                "\"id\": 0,\n" +
                "\"brandCollection\": [\n" +
                "{\n" +
                "\"coverImgUrl\": \"http://tu.yi23.net/brandPage/c72d330ba5a14ff3bdb4dbca9394052b.jpg\",\n" +
                "\"brandZhName\": \"\",\n" +
                "\"brandProfile\": \"百分百Made in NewYork的美国品牌，Taylor Swift等诸多国际女星和名媛都超爱它，1981年成立于纽约，能看到很明显的美式风格，热情、自由、开朗。\",\n" +
                "\"brandId\": 442,\n" +
                "\"isNewArrival\": 0,\n" +
                "\"brandCollectionId\": 67,\n" +
                "\"brandEnName\": \"Necessary Objects\",\n" +
                "\"id\": 0,\n" +
                "\"sort\": 0\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "],\n" +
                "\"hoverItem\": {\n" +
                "\"displayText\": \"\",\n" +
                "\"imageUrl\": \"\",\n" +
                "\"redirectUrl\": \"\"\n" +
                "}\n" +
                "}\n" +
                "}");
//        jsonAdapter.setJsonData("{\"message\":\"ok\",\"nu\":\"11111111111\",\"ischeck\":\"0\",\"condition\":\"00\",\"com\":\"yuantong\",\"status\":\"200\",\"state\":\"5\",\"data\":[{\"time\":\"2018-03-13 11:30:29\",\"ftime\":\"2018-03-13 11:30:29\",\"context\":\"快件已到达黎托街道黎锦苑8栋106号妈妈驿站,如有疑问请联系13755027160\",\"location\":null},{\"time\":\"2018-03-13 11:30:29\",\"ftime\":\"2018-03-13 11:30:29\",\"context\":\"黎托街道黎锦苑8栋106号妈妈驿站已发出自提短信,请上门自提,如有疑问请联系13755027160\",\"location\":null},{\"time\":\"2018-03-10 20:16:41\",\"ftime\":\"2018-03-10 20:16:41\",\"context\":\"国际关系学院妈妈驿站已发出自提短信,请上门自提,如有疑问请联系17610012989\",\"location\":null},{\"time\":\"2018-03-10 20:16:41\",\"ftime\":\"2018-03-10 20:16:41\",\"context\":\"快件已到达国际关系学院妈妈驿站,如有疑问请联系17610012989\",\"location\":null},{\"time\":\"2018-03-09 12:25:35\",\"ftime\":\"2018-03-09 12:25:35\",\"context\":\"孙伯镇红绿灯北800米路东圆通妈妈驿站已发出自提短信,请上门自提,如有疑问请联系18105389267\",\"location\":null},{\"time\":\"2018-03-09 12:25:35\",\"ftime\":\"2018-03-09 12:25:35\",\"context\":\"快件已到达孙伯镇红绿灯北800米路东圆通妈妈驿站,如有疑问请联系18105389267\",\"location\":null},{\"time\":\"2018-03-01 08:17:17\",\"ftime\":\"2018-03-01 08:17:17\",\"context\":\"虹漕路19弄21号华悦家园妈妈驿站已发出自提短信,请上门自提,如有疑问请联系15737044817\",\"location\":null},{\"time\":\"2018-03-01 08:17:17\",\"ftime\":\"2018-03-01 08:17:17\",\"context\":\"快件已到达虹漕路19弄21号华悦家园妈妈驿站,如有疑问请联系15737044817\",\"location\":null},{\"time\":\"2018-01-26 21:59:03\",\"ftime\":\"2018-01-26 21:59:03\",\"context\":\"景富A区东门16路旁圆通快递妈妈驿站已发出自提短信,请上门自提,如有疑问请联系13664776468\",\"location\":null},{\"time\":\"2018-01-26 21:59:03\",\"ftime\":\"2018-01-26 21:59:03\",\"context\":\"快件已到达景富A区东门16路旁圆通快递妈妈驿站,如有疑问请联系13664776468\",\"location\":null},{\"time\":\"2018-01-24 11:37:47\",\"ftime\":\"2018-01-24 11:37:47\",\"context\":\"银领时代2号楼时代超市妈妈驿站已发出自提短信,请上门自提,如有疑问请联系13956927234\",\"location\":null},{\"time\":\"2018-01-24 11:37:27\",\"ftime\":\"2018-01-24 11:37:27\",\"context\":\"快件已到达银领时代2号楼时代超市妈妈驿站,如有疑问请联系13956927234\",\"location\":null},{\"time\":\"2018-01-13 16:17:08\",\"ftime\":\"2018-01-13 16:17:08\",\"context\":\"快件已到达凉城路418号圆通速递公交站后妈妈驿站,如有疑问请联系15801900095\",\"location\":null},{\"time\":\"2018-01-13 16:17:08\",\"ftime\":\"2018-01-13 16:17:08\",\"context\":\"凉城路418号圆通速递公交站后妈妈驿站已发出自提短信,请上门自提,如有疑问请联系15801900095\",\"location\":null},{\"time\":\"2018-01-10 18:11:23\",\"ftime\":\"2018-01-10 18:11:23\",\"context\":\"杨庄南村龙少爷烤鸭旁圆通速递妈妈驿站已发出自提短信,请上门自提,如有疑问请联系15850947167\",\"location\":null},{\"time\":\"2018-01-10 18:11:23\",\"ftime\":\"2018-01-10 18:11:23\",\"context\":\"快件已到达杨庄南村龙少爷烤鸭旁圆通速递妈妈驿站,如有疑问请联系15850947167\",\"location\":null},{\"time\":\"2018-01-07 19:42:41\",\"ftime\":\"2018-01-07 19:42:41\",\"context\":\"圆通合作点【粉丝生活】快件已到达绿地旭辉城小区内物业隔壁粉丝生活服务站驿站,如有疑问请联系18946131929\",\"location\":null},{\"time\":\"2017-12-18 17:17:58\",\"ftime\":\"2017-12-18 17:17:58\",\"context\":\"感恩广场生活区9区2号圆通快递妈妈驿站已发出自提短信,请上门自提,如有疑问请联系15340979298\",\"location\":null},{\"time\":\"2017-12-18 17:17:58\",\"ftime\":\"2017-12-18 17:17:58\",\"context\":\"快件已到达感恩广场生活区9区2号圆通快递妈妈驿站,如有疑问请联系15340979298\",\"location\":null},{\"time\":\"2017-12-18 16:37:45\",\"ftime\":\"2017-12-18 16:37:45\",\"context\":\"快件已到达华林二路40号圆通营业厅妈妈驿站,如有疑问请联系15208326965\",\"location\":null},{\"time\":\"2017-12-18 16:37:45\",\"ftime\":\"2017-12-18 16:37:45\",\"context\":\"华林二路40号圆通营业厅妈妈驿站已发出自提短信,请上门自提,如有疑问请联系15208326965\",\"location\":null},{\"time\":\"2017-12-06 14:43:57\",\"ftime\":\"2017-12-06 14:43:57\",\"context\":\"快件已到达每晚七-十点板泉路大门口自取妈妈驿站,如有疑问请联系021-50831622\",\"location\":null},{\"time\":\"2017-12-06 14:43:57\",\"ftime\":\"2017-12-06 14:43:57\",\"context\":\"每晚七-十点板泉路大门口自取妈妈驿站已发出自提短信,请上门自提,如有疑问请联系021-50831622\",\"location\":null},{\"time\":\"2017-11-30 13:49:45\",\"ftime\":\"2017-11-30 13:49:45\",\"context\":\"快件已到达凡尔赛花园二期二号楼15号门面妈妈驿站,如有疑问请联系023-63417526\",\"location\":null},{\"time\":\"2017-11-30 13:49:45\",\"ftime\":\"2017-11-30 13:49:45\",\"context\":\"凡尔赛花园二期二号楼15号门面妈妈驿站已发出自提短信,请上门自提,如有疑问请联系023-63417526\",\"location\":null},{\"time\":\"2017-11-26 12:45:35\",\"ftime\":\"2017-11-26 12:45:35\",\"context\":\"快件已到达稻香园南区11栋圆通妈妈驿站妈妈驿站,如有疑问请联系15068885505\",\"location\":null},{\"time\":\"2017-11-26 09:14:57\",\"ftime\":\"2017-11-26 09:14:57\",\"context\":\"清新雅苑9号楼成都川菜馆妈妈驿站已发出自提短信,请上门自提,如有疑问请联系13546317781\",\"location\":null},{\"time\":\"2017-11-26 09:14:57\",\"ftime\":\"2017-11-26 09:14:57\",\"context\":\"快件已到达清新雅苑9号楼成都川菜馆妈妈驿站,如有疑问请联系13546317781\",\"location\":null},{\"time\":\"2017-11-18 15:28:25\",\"ftime\":\"2017-11-18 15:28:25\",\"context\":\"海怡嘉园百思特超市妈妈驿站已发出自提短信,请上门自提,如有疑问请联系18353222003\",\"location\":null},{\"time\":\"2017-11-18 15:28:24\",\"ftime\":\"2017-11-18 15:28:24\",\"context\":\"快件已到达海怡嘉园百思特超市妈妈驿站,如有疑问请联系18353222003\",\"location\":null},{\"time\":\"2017-11-12 14:37:23\",\"ftime\":\"2017-11-12 14:37:23\",\"context\":\"颍州南路御景城6栋1单元101妈妈驿站已发出自提短信,请上门自提,如有疑问请联系13855871959\",\"location\":null},{\"time\":\"2017-11-12 14:37:23\",\"ftime\":\"2017-11-12 14:37:23\",\"context\":\"快件已到达颍州南路御景城6栋1单元101妈妈驿站,如有疑问请联系13855871959\",\"location\":null},{\"time\":\"2017-10-30 10:35:13\",\"ftime\":\"2017-10-30 10:35:13\",\"context\":\"阳光后海72栋妈妈驿站妈妈驿站已发出自提短信,请上门自提,如有疑问请联系15581600325\",\"location\":null},{\"time\":\"2017-10-30 10:35:13\",\"ftime\":\"2017-10-30 10:35:13\",\"context\":\"快件已到达阳光后海72栋妈妈驿站妈妈驿站,如有疑问请联系15581600325\",\"location\":null},{\"time\":\"2017-10-26 12:02:51\",\"ftime\":\"2017-10-26 12:02:51\",\"context\":\"老校体育馆南20米路东圆通网点妈妈驿站已发出自提短信,请上门自提,如有疑问请联系18560465196\",\"location\":null},{\"time\":\"2017-10-26 12:02:51\",\"ftime\":\"2017-10-26 12:02:51\",\"context\":\"快件已到达老校体育馆南20米路东圆通网点妈妈驿站,如有疑问请联系18560465196\",\"location\":null},{\"time\":\"2017-10-26 11:23:19\",\"ftime\":\"2017-10-26 11:23:19\",\"context\":\"快件到鑫河湾领域六单元01妈妈驿站已发出自提短信,请上门自提,如有疑问请联系0351-2397126\",\"location\":null},{\"time\":\"2017-10-26 11:23:19\",\"ftime\":\"2017-10-26 11:23:19\",\"context\":\"快件已到达快件到鑫河湾领域六单元01妈妈驿站,如有疑问请联系0351-2397126\",\"location\":null},{\"time\":\"2017-10-23 16:30:34\",\"ftime\":\"2017-10-23 16:30:34\",\"context\":\"插花中学东街口路北十米圆通快递妈妈驿站已发出自提短信,请上门自提,如有疑问请联系18158928623\",\"location\":null},{\"time\":\"2017-10-23 16:30:34\",\"ftime\":\"2017-10-23 16:30:34\",\"context\":\"快件已到达插花中学东街口路北十米圆通快递妈妈驿站,如有疑问请联系18158928623\",\"location\":null},{\"time\":\"2017-10-12 22:11:23\",\"ftime\":\"2017-10-12 22:11:23\",\"context\":\"快件已到达睿城B4栋3单元不便自取请致电妈妈驿站,如有疑问请联系18945009357\",\"location\":null},{\"time\":\"2017-10-12 22:11:23\",\"ftime\":\"2017-10-12 22:11:23\",\"context\":\"睿城B4栋3单元不便自取请致电妈妈驿站已发出自提短信,请上门自提,如有疑问请联系18945009357\",\"location\":null},{\"time\":\"2017-09-26 11:24:53\",\"ftime\":\"2017-09-26 11:24:53\",\"context\":\"南航大活圆通代理点取件妈妈驿站已发出自提短信,请上门自提,如有疑问请联系18014730012\",\"location\":null},{\"time\":\"2017-09-26 11:24:52\",\"ftime\":\"2017-09-26 11:24:52\",\"context\":\"快件已到达南航大活圆通代理点取件妈妈驿站,如有疑问请联系18014730012\",\"location\":null},{\"time\":\"2017-09-01 20:12:51\",\"ftime\":\"2017-09-01 20:12:51\",\"context\":\"环东广场圆通快递云兰租车部妈妈驿站已发出自提短信,请上门自提,如有疑问请联系18552351543\",\"location\":null},{\"time\":\"2017-09-01 20:12:51\",\"ftime\":\"2017-09-01 20:12:51\",\"context\":\"快件已到达环东广场圆通快递云兰租车部妈妈驿站,如有疑问请联系18552351543\",\"location\":null},{\"time\":\"2017-08-30 09:02:31\",\"ftime\":\"2017-08-30 09:02:31\",\"context\":\"圆通合作点【收发室】快件已到达前台驿站,如有疑问请联系13337713618\",\"location\":null},{\"time\":\"2017-08-23 09:12:48\",\"ftime\":\"2017-08-23 09:12:48\",\"context\":\"快件已到达东师人文西校区快递中心妈妈驿站,如有疑问请联系17743125815\",\"location\":null},{\"time\":\"2017-08-23 09:12:48\",\"ftime\":\"2017-08-23 09:12:48\",\"context\":\"东师人文西校区快递中心妈妈驿站已发出自提短信,请上门自提,如有疑问请联系17743125815\",\"location\":null},{\"time\":\"2017-07-24 21:17:01\",\"ftime\":\"2017-07-24 21:17:01\",\"context\":\"快件已到达派送中妈妈驿站,如有疑问请联系63762999\",\"location\":null},{\"time\":\"2017-07-19 11:51:15\",\"ftime\":\"2017-07-19 11:51:15\",\"context\":\"快件已到达寨沙移动公司正对面圆通快递点妈妈驿站,如有疑问请联系13677824226\",\"location\":null},{\"time\":\"2017-06-22 13:26:09\",\"ftime\":\"2017-06-22 13:26:09\",\"context\":\"快件已到达联盟路与向阳交叉南100米圆通妈妈驿站,如有疑问请联系13014796333\",\"location\":null},{\"time\":\"2017-06-22 13:26:09\",\"ftime\":\"2017-06-22 13:26:09\",\"context\":\"联盟路与向阳交叉南100米圆通妈妈驿站已发出自提短信,请上门自提,如有疑问请联系13014796333\",\"location\":null},{\"time\":\"2017-06-07 11:21:03\",\"ftime\":\"2017-06-07 11:21:03\",\"context\":\"快件已到达北服小北门圆通速递妈妈驿站,如有疑问请联系13370110797\",\"location\":null},{\"time\":\"2017-06-07 11:21:03\",\"ftime\":\"2017-06-07 11:21:03\",\"context\":\"北服小北门圆通速递妈妈驿站已发出自提短信,请上门自提,如有疑问请联系13370110797\",\"location\":null}]}");
        rvJson.setAdapter(jsonAdapter);
        btnCollapse.setOnClickListener(this);
        btnExpand.setOnClickListener(this);
        btnGenerateJson.setOnClickListener(this);
        btnShowDialog.setOnClickListener(this);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_modify_layout);
        jsonAdapter.setJsonModifyCallback(new JsonAdapter.JsonModifyCallback() {
            @Override
            public void modify(JsonItemBean jsonItemBean) {
//                if (jsonItemBean.canModify()) {
//                jsonAdapter.setJsonItemViewValue(jsonItemBean, "iam modified");
//                }
                if (jsonItemBean.canDelete()) {
                    jsonAdapter.deleteJsonItem(jsonItemBean);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCollapse) {
            jsonAdapter.collapseAllJsonItems();
        } else if (v.getId() == R.id.btnExpand) {
            jsonAdapter.expandAllJsonItems();
        } else if (v.getId() == R.id.btnGenerateJson) {
            System.out.println(jsonAdapter.generateJson());
        } else if (v.getId() == R.id.btnShowDialog) {
            bottomSheetDialog.show();
        }
    }
}
