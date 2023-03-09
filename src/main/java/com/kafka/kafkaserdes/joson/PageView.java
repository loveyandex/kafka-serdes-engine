package com.kafka.kafkaserdes.joson;

import lombok.AllArgsConstructor;
 
public class PageView  implements JSONSerdeCompatible{
      public String user;
      public String page;
      public Long timestamp;

      public PageView(String user, String page, Long i) {
            this.user = user;
            this.page = page;
            this.timestamp = i;
      }

      public PageView() {
      }
      
      
}


 