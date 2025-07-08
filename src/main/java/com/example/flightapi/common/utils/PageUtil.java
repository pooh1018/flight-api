package com.example.flightapi.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 * 注意：本工具类中的页码均从1开始计数
 * @author
 */
public class PageUtil extends cn.hutool.core.util.PageUtil {

    /**
     * List 分页
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param list 待分页的列表
     * @return 分页后的列表
     */
    public static <T> List<T> paging(int page, int size, List<T> list) {
        if (list == null || list.isEmpty() || size <= 0) {
            return Collections.emptyList();
        }
        
        int fromIndex = page * size;
        int toIndex = fromIndex + size;
        
        if (fromIndex < 0 || fromIndex >= list.size()) {
            return Collections.emptyList();
        }
        
        if (toIndex > list.size()) {
            toIndex = list.size();
        }
        
        return list.subList(fromIndex, toIndex);
    }

    /**
     * MyBatis-Plus IPage 数据处理
     * @param page MyBatis-Plus IPage对象
     * @return 转换后的PageResult对象
     */
    public static <T> PageResult<T> toPage(IPage<T> page) {
        if (page == null) {
            return noData();
        }
        return new PageResult<>(
            page.getRecords(), 
            Math.max(1, (int)page.getCurrent()), 
            Math.max(0, (int)page.getPages()), 
            page.getTotal()
        );
    }

    /**
     * Spring Data Page 数据处理
     * 注意：Spring Data页码从0开始，此方法将其转换为从1开始
     * @param page Spring Data Page对象
     * @return 转换后的PageResult对象
     */
    public static <T> PageResult<T> toPage(Page<T> page) {
        if (page == null) {
            return noData();
        }
        return new PageResult<>(
            page.getContent(),
            page.getNumber() + 1,  // Spring Data页码从0开始，转换为从1开始
            page.getTotalPages(),
            page.getTotalElements()
        );
    }

    /**
     * 将列表转换为单页的PageResult
     * @param list 数据列表
     * @return 包含单页数据的PageResult对象
     */
    public static <T> PageResult<T> toPage(List<T> list) {
        if (list == null) {
            return noData();
        }
        return new PageResult<>(list, 1, 1, list.size());
    }

    /**
     * 返回空数据的PageResult
     * @return 空数据的PageResult对象
     */
    public static <T> PageResult<T> noData() {
        return new PageResult<>(Collections.emptyList(), 1, 0, 0);
    }

    /**
     * 自定义分页（只包含内容和总记录数）
     * @param list 数据列表
     * @param totalElements 总记录数
     * @return 自定义的PageResult对象
     */
    public static <T> PageResult<T> toPage(List<T> list, long totalElements) {
        if (list == null) {
            return noData();
        }
        return new PageResult<>(list, totalElements);
    }

    /**
     * 自定义完整分页
     * @param list 数据列表
     * @param currentPage 当前页码（从1开始）
     * @param totalPages 总页数
     * @param totalElements 总记录数
     * @return 自定义的PageResult对象
     */
    public static <T> PageResult<T> toPage(List<T> list, int currentPage, int totalPages, long totalElements) {
        if (list == null) {
            list = Collections.emptyList();
        }
        currentPage = Math.max(1, currentPage);
        totalPages = Math.max(0, totalPages);
        totalElements = Math.max(0, totalElements);
        
        return new PageResult<>(list, currentPage, totalPages, totalElements);
    }
    
    /**
     * 计算总页数
     * @param totalElements 总记录数
     * @param pageSize 每页大小
     * @return 总页数
     */
    public static int calculateTotalPages(long totalElements, int pageSize) {
        if (pageSize <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalElements / pageSize);
    }
    
    /**
     * 将PageResult<T>转换为PageResult<R>
     * @param page 原PageResult对象
     * @param converter 类型转换函数
     * @return 转换后的PageResult对象
     */
    public static <T, R> PageResult<R> convert(PageResult<T> page, Function<T, R> converter) {
        if (page == null || converter == null) {
            return noData();
        }
        
        List<R> convertedContent = page.getContent() == null ? 
            Collections.emptyList() : 
            page.getContent().stream()
                .map(converter)
                .collect(Collectors.toList());
                
        return new PageResult<>(
            convertedContent, 
            page.getCurrentPage(), 
            page.getTotalPages(), 
            page.getTotalElements()
        );
    }
    
    /**
     * 根据页码和每页大小创建分页参数
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小
     * @return 分页参数数组，第一个元素为页码（从0开始），第二个元素为每页大小
     */
    public static int[] of(int pageNum, int pageSize) {
        pageNum = Math.max(1, pageNum);
        pageSize = Math.max(1, pageSize);
        return new int[]{pageNum - 1, pageSize}; // 转换为从0开始的页码
    }
}
