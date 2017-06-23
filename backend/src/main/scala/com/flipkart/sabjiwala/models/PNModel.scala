package com.flipkart.sabjiwala.models

import akka.http.scaladsl.model.headers.CacheDirectives.public
import com.fasterxml.jackson.databind.node.ObjectNode

case class PNModel (channel:String, expiryTs:Long,channelInfo:ChannelInfo,sla:String, contextId:String, channelData:ChannelData)

case class ChannelInfo (`type`:String,deviceIds:List[String])

case class ChannelData()
