<?xml version="1.0" encoding="UTF-8"?>
<tileset name="default" tilewidth="64" tileheight="64" tilecount="4" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="1">
  <image width="64" height="64" source="background1.png"/>
 </tile>
 <tile id="2">
  <properties>
   <property name="destructible" type="bool" value="true"/>
   <property name="health" type="int" value="100"/>
   <property name="solid" type="bool" value="true"/>
  </properties>
  <image width="64" height="64" source="destructible1.png"/>
 </tile>
 <tile id="3">
  <properties>
   <property name="solid" type="bool" value="true"/>
  </properties>
  <image width="64" height="64" source="indestructible1.png"/>
 </tile>
 <tile id="4">
  <properties>
   <property name="spawn" type="bool" value="true"/>
  </properties>
  <image width="64" height="64" source="background1.png"/>
 </tile>
</tileset>
