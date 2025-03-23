package com.pwc.portal.dao;

import com.pwc.portal.domain.RetryMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RetryMessageDao {
    /**
     * 插入重试记录
     */
    @Insert("INSERT INTO retry_message (order_id, message_id, retry_count, status, delay_time, create_time) " +
            "VALUES (#{orderId}, #{messageId}, #{retryCount}, #{status}, #{delayTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RetryMessage retryMessage);

    /**
     * 根据 messageId 更新状态
     */
    @Update("UPDATE retry_message SET status = #{status} WHERE message_id = #{messageId}")
    int updateStatus(@Param("messageId") String messageId, @Param("status") Integer status);

    /**
     * 根据 messageId 增加重试次数
     */
    @Update("UPDATE retry_message SET retry_count = retry_count + 1 WHERE message_id = #{messageId}")
    int incrementRetryCount(@Param("messageId") String messageId);

    /**
     * 根据 messageId 查询记录
     */
    @Select("SELECT * FROM retry_message WHERE message_id = #{messageId}")
    RetryMessage selectByMessageId(@Param("messageId") String messageId);

    /**
     * 查询所有需重试的记录（status=0 且 retry_count < 3）
     */
    @Select("SELECT * FROM retry_message WHERE status = 0 AND retry_count < 3")
    List<RetryMessage> selectPendingMessages();
}