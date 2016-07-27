

/**
 * 2016-05-17 14:34:20
 * 
 * @author yao.zhang
 *
 */
public class EditorHelpInfoPage extends BaseCustomPopupTemplate implements
		IsSerializable {
	transient Frame mapFrame;
	transient static EditorHelpInfoPage editorHelpInfoPage;
	
	public EditorHelpInfoPage() {
	}

	public void draw(VerticalPanel content) {
		composites = new Vector();
		initTMSJS();
		mapFrame = new Frame("thirdparty/UEditor/index.html");
		editorHelpInfoPage =  this;
		Panel framePanel = new Panel();
		framePanel.setLayout(new FitLayout());
		framePanel.setSize(pageConfig.getPageWidth(), pageConfig.getPageHeight());
		framePanel.setWidth(pageConfig.getPageWidth());
		framePanel.setHeight(pageConfig.getPageHeight());
		framePanel.setBodyBorder(false);
		framePanel.setBorder(false);
		framePanel.setAutoScroll(false);
		framePanel.add(mapFrame);
		
		Panel container = new Panel();
		container.setSize(pageConfig.getPageWidth(), pageConfig.getPageHeight());
		container.setLayout(new BorderLayout());
    	container.setBorder(false);
    	
    	BorderLayoutData centerLayoutData = new BorderLayoutData(RegionPosition.CENTER);
    	centerLayoutData.setMargins(new Margins(0, 0, 0, 0));
        
        container.add(framePanel, centerLayoutData);

		content.add(container);
	}
	
	public void init(){
		if(params.get(IPage.ENTITY_ID) == null){
			return;
		}
		AsyncCallBackAdapter callBack = new AsyncCallBackAdapter() {

			protected void exec() {
				CommitServiceAsync serviceAsync = AjaxServiceUtil
						.initialAsyncService(ConfigContext.DEFAULT);
				serviceAsync.executeCustom(HelpInfoConstant.PO_INFO_EDIT_MANAGER, HelpInfoConstant.FIND_HELP_INFO_BY_ID, params, this);
			}

			public void onSuccess() {
				Map map = (Map) result;

				// 设置控件值
				map.remove(Constants.THORN_MESSAGE_KEY);
				if(map.get(HelpInfoConstant.ID) != null 
						&& map.get(HelpInfoConstant.SORT_NUM) != null
						&& map.get(HelpInfoConstant.TITLE) != null
						&& map.get(HelpInfoConstant.CONTENT) != null){
					findHelpInfoById(mapFrame.getElement(),map.get(HelpInfoConstant.ID).toString()
							,map.get(HelpInfoConstant.SORT_NUM).toString()
							,map.get(HelpInfoConstant.TITLE).toString()
							,map.get(HelpInfoConstant.CONTENT).toString());
				}
			
			}
		};
		callBack.exec("");
	}
	
	public static void initDateById(){
		editorHelpInfoPage.init();
	}
	public static void storeHelpInfo(final String id,final String sortNum,final String title,final String content){
		AsyncCallBackAdapter callBack = new AsyncCallBackAdapter(){

			protected void exec() {
				Map<String,Object> params = new HashMap<String, Object>();
				params.put(HelpInfoConstant.ID, id);
				params.put(HelpInfoConstant.SORT_NUM, sortNum);
				params.put(HelpInfoConstant.TITLE, title);
				params.put(HelpInfoConstant.CONTENT, content);
				CommitServiceAsync serviceAsync = AjaxServiceUtil.initialAsyncService(ConfigContext.DEFAULT);
				serviceAsync.executeCustom(HelpInfoConstant.PO_INFO_EDIT_MANAGER, HelpInfoConstant.STORE_INFO, params,this);
			}

			public void onSuccess() {
//				MessageBox.alert("保存成功");
			}
		};
		callBack.exec("");
	}
	
	public static native void initTMSJS()/*-{
		$wnd.storeHelpInfo = function(id,sortNum,title,content) {
		@com.vtradex.tms.client.editor.EditorHelpInfoPage::storeHelpInfo(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(id,sortNum,title,content);
	};
		$wnd.initDateById = function() {
		@com.vtradex.tms.client.editor.EditorHelpInfoPage::initDateById()();
	};
	}-*/;
	
	protected native void findHelpInfoById(Element element ,String id,String sortNum ,String title , String content ) /*-{
	element.contentWindow.findHelpInfoById(id , sortNum , title , content );
}-*/;

	
}
