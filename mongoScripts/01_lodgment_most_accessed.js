db.getCollection("audit_logs").aggregate(
    [
        {
            "$group" : {
                "_id" : "$lodgementId",
                "count" : {
                    "$sum" : NumberInt(1)
                }
            }
        }, 
        {
            "$sort" : {
                "count" : NumberInt(1)
            }
        }, 
        {
            "$limit" : NumberInt(1)
        }
    ]
);
