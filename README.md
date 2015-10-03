# Hapme
Haptics Communication System

## To Do List
* ~~View members in the channel in Operation page. (should auto update when a member joins)~~
* ~~Receiver for notifications~~
* ~~Only Commander should be able to send the commands he created (currently all members are able to send all commands)~~
* ~~Member default commands~~
* ~~Commander need to be able to send Commands using Myo Armband~~
* ~~Set Vibration sequence for commands and notifications~~
* ~~Watch App~~
* ~~Need to make UI buttons (View Location button, Edit Operation button,'commander callsign' textview) display accordingly to the user profile (commander and members)~~
* ~~Change font color according to background for watch~~
* Loading Status for Create and Join Operation
* Watch onResume needs to set vibration and replace screen
* Duplicate member call sign check
* ~~When end operation, need to remove all members in the operation (bug)~~
* ~~Delete command after operation ends (bug)~~

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

##Naming of activity and xml layout
#####(its a total burden to think of which activity belong to which layout, so....)
* AddCmdActivity.java => add_cmd.xml
* CreateOperationActivity => create_operation.xml
* CommandsActivity => commands.xml
* HomeActivity => home.xml
* JoinOps => join_ops.xml
* OperationActivity => operation.xml

##Naming of custom layout files
#####(since there is already one)
* cust_xxx

##Future Enhancement
* Piggyback Google maps Location
* Web application for Command Post to analyse commands sent + logging
* Commander need to be able to edit Operation/Command details, remove members
* Commander need to be able to edit Operation details, remove members