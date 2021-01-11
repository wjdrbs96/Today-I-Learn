# `sequelize에서 query 사용하는 법`

```javascript
const db = require('../models');
const sequelize = require('sequelize');

module.exports = {
  findAllGroupList: async (offset) => {
    try {
      const query = `SELECT g.groupName, g.maximumMemberNumber, m.GroupId, image.groupImageUrl, count(m.UserId) as memberCount FROM MEANING.Group g 
                    JOIN GroupProfile p ON g.id = p.GroupId JOIN Member m ON g.id = m.GroupId JOIN GroupImage image ON p.GroupImageId = image.id
                    GROUP BY g.id ORDER BY memberCount DESC LIMIT ${offset}, 1`;

      const result = await db.sequelize.query(query, {
        type: sequelize.QueryTypes.SELECT,
      });
      return result;
    } catch (error) {
      throw error;
    }
  },
};
```

시퀄라이즈에서 데이터 구조가 복잡하고 설계 하기가 힘들 때 쿼리를 사용하는 방법이 있습니다.(또는 성능 튜닝을 할 때나..)

그러면 위와 같은 방법으로 `db, sequelize`를 require 해서 사용할 수 있습니다.
