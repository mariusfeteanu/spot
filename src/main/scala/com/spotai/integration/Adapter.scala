package com.spotai.integration

trait Adapter{
  def listen():Unit
  def stop():Unit
}