# Hapme
Haptics Communication System

## Naming Conventions
###### (Do add on to it if you want to standardize something)
* Buttons = bn_xxx (e.g bn_createMe)
* Image = img_xxx (e.g img_bn_create, img_banner)
* EditText = et_xxx (e.g et_gesture1, et_create)
* TextView = tv
* ListView = lv
* Others = Try to use two letters to summarize the object, followed by an underscore with the description

##Abbreviation
#####(if there is a need to use shortform, do try to follow the list below)
* ops - operation(s)
* cmd - command
* 

##Naming of activity and xml layout
#####(its a total burden to think of which activity belong to which layout, so....)
* AddCmdActivity.java => add_cmd.xml
* CreateOperationActivity => create_operation.xml
* CommandsActivity => commands.xml
* HomeActivity => home.xml
* JoinOps => join_ops.xml
* OperationActivity => operation.xml
* 

##Naming of custom layout files
#####(since there is already one)
* cust_xxxxxxxxx
* 

## To Do List
* ~~View members in the channel in Operation page. (should auto update when a member joins)~~
* ~~Receiver for notifications~~
* ~~Only Commander should be able to send the commands he created (currently all members are able to send all commands)~~
* ~~Member default commands~~
* Set Vibration sequence for commands and notifications
* Commander need to be able to send Commands using Myo Armband
* Commander need to be able to edit Operation details, remove members
* Need to make UI buttons (View Location button, Edit Operation button) display accordingly to the user profile (commander and members)
* Watch App
* Simple web app for Command Post to view all the commands sent, highlight to them when there is an "high alert" command sent.
* GPS and Google Map location (KIV)
