package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.mapper.MediaItemMapper;
import com.modelink.reservation.service.MediaItemService;
import com.modelink.reservation.vo.MediaItemParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MediaItemServiceImpl implements MediaItemService {

    @Resource
    private MediaItemMapper mediaItemMapper;

    /**
     * 插入一条承保记录
     *
     * @param mediaItem
     * @return
     */
    @Override
    public int insert(MediaItem mediaItem) {
        return mediaItemMapper.insertSelective(mediaItem);
    }

    /**
     * 更新一条记录
     *
     * @param mediaItem
     * @return
     */
    @Override
    public int update(MediaItem mediaItem) {
        return mediaItemMapper.updateByPrimaryKeySelective(mediaItem);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param mediaItem
     * @return
     */
    @Override
    public int countByParam(MediaItem mediaItem) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param mediaItem
     * @return
     */
    public MediaItem findOneByParam(MediaItem mediaItem) {
        List<MediaItem> mediaItemList = mediaItemMapper.select(mediaItem);
        if(mediaItemList != null && mediaItemList.size() > 0){
            return mediaItemList.get(0);
        }
        return null;
    }

    /**
     * 查询符合条件的记录列表
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public List<MediaItem> findListByParam(MediaItemParamPagerVo paramPagerVo) {
        Example example = new Example(MediaItem.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.hasText(paramPagerVo.getColumnFieldIds())){
            example.selectProperties(paramPagerVo.getColumnFieldIds().split(","));
        }
        String dateField = paramPagerVo.getDateField();
        if(StringUtils.isEmpty(dateField)){
            dateField = "date";
        }
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
        }
        if(StringUtils.hasText(paramPagerVo.getPlatformName())){
            if("OTHER".equals(paramPagerVo.getPlatformName())) {
                List<String> list = new ArrayList<>();
                list.add("PC");
                list.add("WAP");
                criteria.andEqualTo("platformName", paramPagerVo.getPlatformName());
            }else{
                criteria.andEqualTo("platformName", paramPagerVo.getPlatformName());
            }
        }
        if(StringUtils.hasText(paramPagerVo.getAdvertiseActive())){
            criteria.andIn("advertiseActive", Arrays.asList(paramPagerVo.getAdvertiseActive().split(",")));
        }
        if(StringUtils.hasText(paramPagerVo.getFeeType())){
            criteria.andEqualTo("feeType", paramPagerVo.getFeeType());
        }
        List<MediaItem> mediaItemList = mediaItemMapper.selectByExample(example);
        return mediaItemList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<MediaItem> findPagerByParam(MediaItemParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(MediaItem.class);
        Example.Criteria criteria = example.createCriteria();
        String dateField = paramPagerVo.getDateField();
        if(StringUtils.isEmpty(dateField)){
            dateField = "date";
        }
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
        }
        example.setOrderByClause(dateField + " desc");
        List<MediaItem> mediaItemList = mediaItemMapper.selectByExample(example);
        PageInfo<MediaItem> pageInfo = new PageInfo<>(mediaItemList);
        return pageInfo;
    }
}
