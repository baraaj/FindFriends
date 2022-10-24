package issat.baraa.findfriends;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MySmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String messageBody, phoneNumber;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > -1) {
                    messageBody = messages[0].getMessageBody();
                    phoneNumber = messages[0].getDisplayOriginatingAddress();
                    Toast.makeText(context,
                            "Message : " + messageBody + "Reçu de la part de;" + phoneNumber,
                            Toast.LENGTH_LONG).show();
                   //if(messageBody == "envoyer votre position") {
                      if(messageBody.contains("FindFriends: envoyer votre position"))  {
                          //lancer service
                          //capter ma position gps et l'envoyer ver ce numero
                          Intent i=new Intent(context,MyLocationService.class);
                          i.putExtra("numero",phoneNumber);
                          context.startService(i);


                    }
                      if(messageBody.contains("FindFriends: ma position est :")) {
                          //recuperer la position et la renvoyer vers phone number
                            String[]t=messageBody.split("#");
                            String longitude=t[1];
                            String latitude=t[2];
                          //lancer une notification dans le canal myapplication_channel
                          NotificationCompat.Builder mynotif = new NotificationCompat.Builder(
                                  context,
                                  "findfriends_channel");
                          mynotif.setContentTitle("position recu!");
                          mynotif.setContentText("appuyer pour voir.");
                          mynotif.setSmallIcon(android.R.drawable.ic_dialog_map);
                          mynotif.setAutoCancel(true);
                           // action pour la notification
                          Intent map=new Intent(context,MapsActivity.class);
                          map.putExtra("longitude",longitude);
                          map.putExtra("latitude",latitude);
                          PendingIntent pi=PendingIntent.getActivity(context,1,map,PendingIntent.FLAG_IMMUTABLE);
                          mynotif.setContentIntent(pi);


                          mynotif.setVibrate(new long[]{500, 1000, 200, 2000});
                          // il faut ajouter la permission VIBRATE dans le manifest
                          Uri son = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                          mynotif.setSound(son);
                          // instance du gestionnaire des notifications de l'appareil
                          NotificationManagerCompat manager =
                                  NotificationManagerCompat.from(context);
                          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
/* creation du canal si la version android de l'appareil est supérieur à
Oreo */
                              NotificationChannel canal = new
                                      NotificationChannel("findfriends_channel",
                                      // l'ID exacte du canal
                                      "canal pour lapplication find me",
                                      NotificationManager.IMPORTANCE_DEFAULT);
                              AudioAttributes attr = new AudioAttributes.Builder()

                                      .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                      .setUsage(AudioAttributes.USAGE_ALARM)
                                      .build();
                              // ajouter du son pour le canal
                              canal.setSound(son, attr);
                              // creation du canal dans l'appareil
                              manager.createNotificationChannel(canal);
                          }
                          // lancement de la notification
                          manager.notify(0, mynotif.build());

                      }
                    }
                   else{
                       System.out.print("version invalid");
                   }
                }

        }
    }
}