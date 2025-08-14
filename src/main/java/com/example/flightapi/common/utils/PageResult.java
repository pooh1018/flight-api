package com.example.flightapi.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 * @author
 * @param <T> 分页内容的类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    /**
     * 分页内容
     */
    private List<T> content;

    /**
     * 当前页码（从1开始）
     */
    private int currentPage;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 总记录数
     */
    private long totalElements;

    /**
     * 创建只包含内容和总记录数的分页结果
     * @param content 数据列表
     * @param totalElements 总记录数
     */
    public PageResult(List<T> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
        this.currentPage = 1;
        this.totalPages = 1;
    }
    
    /**
     * 创建包含内容、总记录数和页大小的分页结果
     * @param content 数据列表
     * @param totalElements 总记录数
     * @param pageSize 每页大小
     */
    public PageResult(List<T> content, long totalElements, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.currentPage = 1;
        this.totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 1;
    }
    
    /**
     * 判断结果是否为空
     * @return 如果内容为null或空列表，则返回true
     */
    public boolean isEmpty() {
        return content == null || content.isEmpty();
    }
    
    /**
     * 判断是否有下一页
     * @return 如果当前页小于总页数，则返回true
     */
    public boolean hasNext() {
        return currentPage < totalPages;
    }
    
    /**
     * 判断是否有上一页
     * @return 如果当前页大于1，则返回true
     */
    public boolean hasPrevious() {
        return currentPage > 1;
    }
}
