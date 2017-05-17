package org.bigbluebutton.client

import akka.actor.{Actor, ActorLogging, Props}
import org.bigbluebutton.client.bus.{JsonMsgToAkkaAppsBus, JsonMsgToAkkaAppsBusMsg, JsonMsgToSendToAkkaApps}
import org.bigbluebutton.common2.messages.BbbServerMsg
import org.bigbluebutton.common2.util.JsonUtil

object MsgToAkkaAppsToJsonActor {
  def props(jsonMsgToAkkaAppsBus: JsonMsgToAkkaAppsBus): Props =
    Props(classOf[MsgToAkkaAppsToJsonActor], jsonMsgToAkkaAppsBus)

}
class MsgToAkkaAppsToJsonActor(jsonMsgToAkkaAppsBus: JsonMsgToAkkaAppsBus)
  extends Actor with ActorLogging with SystemConfiguration {

  def receive = {
    case msg: BbbServerMsg => handle(msg)
  }

  def handle(msg: BbbServerMsg): Unit = {
    val json = JsonUtil.toJson(msg)
    val jsonMsg = JsonMsgToSendToAkkaApps(toAkkaAppsRedisChannel, json)
    jsonMsgToAkkaAppsBus.publish(JsonMsgToAkkaAppsBusMsg(toAkkaAppsJsonChannel, jsonMsg))
  }

}
