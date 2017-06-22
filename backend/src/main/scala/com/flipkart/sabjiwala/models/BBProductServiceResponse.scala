package com.flipkart.sabjiwala.models

;

/**
  * Created by saurabh.mimani on 22/06/17.
  */
case class BbProductServiceResponse(
                                     results: BbResult
                                   )

case class BbResult(
                    data: List[BbData]
                   )

case class BbData(
                   actual_weight: String,
                   name: String,
                   pid:String,
                   weight:String,
                   price:String
                 )