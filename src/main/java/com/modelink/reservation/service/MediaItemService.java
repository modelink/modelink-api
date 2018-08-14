package com.modelink.reservation.service;

import com.github.pagehelper.PageInfo;
import com.modelink.reservation.bean.MediaItem;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.vo.MediaItemParamPagerVo;

import java.util.List;

/**
 * 承保数据服务接口
 */
public interface MediaItemService {

    /**
     * 插入一条承保记录
     * @param mediaItem
     * @return
     */
    public int insert(MediaItem mediaItem);

    /**
     * 更新一条记录
     * @param mediaItem
     * @return
     */
    public int update(MediaItem mediaItem);

    /**
     * 查询符合条件的记录总数
     * @param mediaItem
     * @return
     */
    public int countByParam(MediaItem mediaItem);

    /**
     * 查询符合条件的记录
     * @param mediaItem
     * @return
     */
    public MediaItem findOneByParam(MediaItem mediaItem);

    /**
     * 查询符合条件的记录列表
     * @param paramPagerVo
     * @return
     */
    public List<MediaItem> findListByParam(MediaItemParamPagerVo paramPagerVo);

    /**
     * 查询符合条件的记录列表（分页查询）
     * @param paramPagerVo
     * @return
     */
    public PageInfo<MediaItem> findPagerByParam(MediaItemParamPagerVo paramPagerVo);
}
