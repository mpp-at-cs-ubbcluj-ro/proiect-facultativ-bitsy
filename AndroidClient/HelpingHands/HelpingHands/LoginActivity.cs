using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Google.Android.Material.BottomNavigation;
using HelpingHands.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using static Android.Service.Voice.VoiceInteractionSession;

namespace HelpingHands
{
    [Activity(Label = "Login", Theme = "@style/AppTheme", MainLauncher = true)]    
    public class LoginActivity : Activity
    {
        EditText PasswordBox;
        EditText UsernameBox;
        Button LoginButton;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_login);

            PasswordBox = FindViewById<EditText>(Resource.Id.PasswordBox);
            UsernameBox = FindViewById<EditText>(Resource.Id.UsernameBox);
            LoginButton = FindViewById<Button>(Resource.Id.LoginButton);
            
            LoginButton.Click += LoginButton_Clicked;
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        public async void LoginButton_Clicked(object sender, EventArgs e)
        {                       
            var username = UsernameBox.Text;
            var password = PasswordBox.Text;

            try
            {
                var userInfo = await API.Client.Login(username, password);
                await MessageBox.Alert(this, $"User = {userInfo.Type}:{userInfo.User.Nume} {userInfo.User.Prenume}");

                if (userInfo.Type == "Voluntar")
                {
                    Intent intent = new Intent(this, typeof(MainVoluntarActivity));
                    StartActivity(intent);
                }
                else
                {
                    await MessageBox.Alert(this, "Admins cannot connect through app");
                }
            }
            catch(Exception ex)
            {
                await MessageBox.Alert(this, ex.Message, "Login failed");
                return;
            }                        
        }
    }
}