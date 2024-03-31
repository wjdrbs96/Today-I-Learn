## `MyBatis Bulk Insert 하는 법`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.mapper.TestMapper">
    <insert id="bulkInsert" parameterType="com.example.mapper.entity.TestEntity">
        insert into test_entity
        (
        name,
        part,
        created_at,
        updated_at,
        )
        values
        <foreach collection="list" index="index" item="test" separator=",">
            (
            #{test.name},
            #{test.part},
            NOW(),
            NOW()
            )
        </foreach>
    </insert>
</mapper>
```