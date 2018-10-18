package com.modelink.reservation.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.modelink.admin.vo.dashboard.DashboardParamVo;
import com.modelink.reservation.bean.Underwrite;
import com.modelink.reservation.mapper.UnderwriteMapper;
import com.modelink.reservation.service.UnderwriteService;
import com.modelink.reservation.vo.UnderwriteParamPagerVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class UnderwriteServiceImpl implements UnderwriteService {

    @Resource
    private UnderwriteMapper underwriteMapper;

    /**
     * 插入一条承保记录
     *
     * @param underwrite
     * @return
     */
    @Override
    public int insert(Underwrite underwrite) {
        return underwriteMapper.insertSelective(underwrite);
    }

    /**
     * 更新一条记录
     * @param underwrite
     * @return
     */
    @Override
    public int update(Underwrite underwrite) {
        return underwriteMapper.updateByPrimaryKey(underwrite);
    }

    /**
     * 查询符合条件的记录总数
     *
     * @param underwrite
     * @return
     */
    @Override
    public int countByParam(Underwrite underwrite) {
        return 0;
    }

    /**
     * 查询符合条件的记录
     * @param underwrite
     * @return
     */
    public Underwrite findOneByParam(Underwrite underwrite) {
        List<Underwrite> underwriteList = underwriteMapper.select(underwrite);
        if(underwriteList != null && underwriteList.size() > 0){
            return underwriteList.get(0);
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
    public List<Underwrite> findListByParam(UnderwriteParamPagerVo paramPagerVo) {
        Example example = new Example(Underwrite.class);
        if(StringUtils.hasText(paramPagerVo.getColumnFieldIds())) {
            example.selectProperties(paramPagerVo.getColumnFieldIds().split(","));
        }
        String dateField = paramPagerVo.getDateField();
        if(StringUtils.isEmpty(dateField)){
            dateField = "finishDate";
        }
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
        }
        if(!StringUtils.isEmpty(paramPagerVo.getMerchantId())){
            criteria.andEqualTo("merchantId", paramPagerVo.getMerchantId());
        }
        if(!StringUtils.isEmpty(paramPagerVo.getMobile())) {
            criteria.andEqualTo("reserveMobile", paramPagerVo.getMobile());
        }
        if(paramPagerVo.getMobiles() != null && paramPagerVo.getMobiles().size() > 0){
            criteria.andIn("reserveMobile", paramPagerVo.getMobiles());
        }
        if(StringUtils.hasText(paramPagerVo.getPlatformName())){
            if("OTHER".equals(paramPagerVo.getPlatformName())) {
                List<String> list = new ArrayList<>();
                list.add("PC");
                list.add("WAP");
                criteria.andNotIn("platformName", list);
            }else{
                criteria.andEqualTo("platformName", paramPagerVo.getPlatformName());
            }
        }
        if(StringUtils.hasText(paramPagerVo.getAdvertiseActive())){
            criteria.andIn("advertiseActive", Arrays.asList(paramPagerVo.getAdvertiseActive().split(",")));
        }
        if(paramPagerVo.getProvinceId() != null){
            criteria.andEqualTo("provinceId", paramPagerVo.getProvinceId());
        }
        if(StringUtils.hasText(paramPagerVo.getSource())){
            if(paramPagerVo.getSource().startsWith("!")){
                criteria.andNotEqualTo("source", paramPagerVo.getSource());
                criteria.andNotEqualTo("source", "非平台数据");
                criteria.andNotEqualTo("source", "其他");
                criteria.andNotLike("source", "%加保%");
            }else {
                criteria.andEqualTo("source", paramPagerVo.getSource());
            }
        }
        List<Underwrite> underwriteList = underwriteMapper.selectByExample(example);
        return underwriteList;
    }

    /**
     * 查询符合条件的记录列表
     * @param mobileSet
     * @param sortField
     * @return
     */
    @Override
    public List<Underwrite> findListByMobiles(Set<String> mobileSet, String sortField) {
        if(mobileSet == null || mobileSet.size() <= 0){
            return new ArrayList<>();
        }
        Example example = new Example(Underwrite.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("reserveMobile", mobileSet);
        if(StringUtils.hasText(sortField)) {
            example.setOrderByClause(sortField);
        }
        List<Underwrite> underwriteList = underwriteMapper.selectByExample(example);
        return underwriteList;
    }

    /**
     * 查询符合条件的记录列表
     * @param insuranceNoSet
     * @return
     */
    public List<Underwrite> findListByInsuranceNoSet(Set<String> insuranceNoSet, String filteFields) {
        if(insuranceNoSet == null || insuranceNoSet.size() <= 0){
            return new ArrayList<>();
        }
        Example example = new Example(Underwrite.class);
        if(StringUtils.hasText(filteFields)) {
            example.selectProperties(filteFields.split(","));
        }

        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("insuranceNo", insuranceNoSet);

        List<Underwrite> underwriteList = underwriteMapper.selectByExample(example);
        return underwriteList;
    }

    /**
     * 查询符合条件的记录列表（分页查询）
     *
     * @param paramPagerVo
     * @return
     */
    @Override
    public PageInfo<Underwrite> findPagerByParam(UnderwriteParamPagerVo paramPagerVo) {
        PageHelper.startPage(paramPagerVo.getPageNo(), paramPagerVo.getPageSize());

        Example example = new Example(Underwrite.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.hasText(paramPagerVo.getColumnFieldIds())) {
            example.selectProperties(paramPagerVo.getColumnFieldIds().split(","));
        }
        String dateField = paramPagerVo.getDateField();
        if(StringUtils.isEmpty(dateField)){
            dateField = "finishDate";
        }
        if(!StringUtils.isEmpty(paramPagerVo.getChooseDate()) && paramPagerVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramPagerVo.getChooseDate().split(" - ");
            criteria.andLessThanOrEqualTo(dateField, chooseDates[1]);
            criteria.andGreaterThanOrEqualTo(dateField, chooseDates[0]);
        }
        if(!StringUtils.isEmpty(paramPagerVo.getMobile())) {
            criteria.andEqualTo("mobile", paramPagerVo.getMobile());
        }
        example.setOrderByClause("create_time desc");
        List<Underwrite> underwriteList = underwriteMapper.selectByExample(example);
        PageInfo<Underwrite> pageInfo = new PageInfo<>(underwriteList);
        return pageInfo;
    }


    /**
     * 获取指定日期内的数据（只查日期与联系方式两列，节省内存）
     * @param paramVo
     * @return
     */
    @Override
    public List<Underwrite> findListWithLimitColumnByDateRange(DashboardParamVo paramVo) {
        String startDate = "";
        String endDate = "";
        if(!StringUtils.isEmpty(paramVo.getChooseDate()) && paramVo.getChooseDate().contains(" - ")){
            String[] chooseDates = paramVo.getChooseDate().split(" - ");
            startDate = chooseDates[0];
            endDate = chooseDates[1];
        }
        return underwriteMapper.findListWithLimitColumnByDateRange(startDate, endDate, paramVo.getMerchantId());
    }
}
