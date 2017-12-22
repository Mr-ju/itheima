package cn.e3.search.service.impl;

import cn.e3.search.dao.SearchItemDao;
import cn.e3.search.mapper.SearchItemMapper;
import cn.e3.search.pojo.SearchItem;
import cn.e3.search.pojo.SolrPage;
import cn.e3.search.service.SearchItemService;
import cn.e3.utils.E3mallResult;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by GliangJu on 2017/12/21.
 */
@Service
public class SearchItemServiceImpl implements SearchItemService  {

    //实现索引库数据写入,必须注入solr服务
    @Autowired
    private SolrServer solrServer;

    //注入搜索mapper接口代理对象
    @Autowired
    private SearchItemMapper searchItemMapper;

    //注入dao
    @Autowired
    private SearchItemDao searchItemDao;

    /**
     * 需求:询索引库域字段对应数据库值写入索引库
     * 参数:无
     * 参数:E3mallResult
     * 思考:服务发布?
     */
    @Override
    public E3mallResult findDatabaseToSolrIndex() {
        try {
            // 询索引库域字段对应数据库值
            List<SearchItem> list = searchItemMapper.findDatabaseToSolrIndex();
            //循环集合数据,把数据封装到doc文档对象,实现索引库写入
            for (SearchItem searchItem : list){
                //创建一个文档对象,封装索引域字段对应值
                SolrInputDocument doc = new SolrInputDocument();
                //封装文档域所对应数据库查询值
                doc.addField("id",searchItem.getId());
                //标题
                doc.addField("item_title",searchItem.getTitle());
                // 买点
                doc.addField("item_sell_point",searchItem.getSell_point());
                //价格
                doc.addField("item_price",searchItem.getPrice());
                //图片地址
                doc.addField("item_image",searchItem.getImage());
                //商品类别
                doc.addField("item_category_name",searchItem.getCategory_name());
                //商品描述
                doc.addField("item_desc",searchItem.getItem_desc());
                //写入索引库
                solrServer.add(doc);
            }
            //提交
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回
        return E3mallResult.ok();
    }

    /**
     * 需求:接受参数,封装参数查询索引库
     * 参数:String qName
     * 返回值:SolrPage
     */
    @Override
    public SolrPage findSolrIndex(String qName, Integer page, Integer rows) {

        return null;
    }
}