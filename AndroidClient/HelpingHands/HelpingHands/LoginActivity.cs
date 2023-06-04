using Android.App;
using Android.Content;
using Android.Graphics;
using Android.OS;
using Android.Runtime;
using Android.SE.Omapi;
using Android.Views;
using Android.Widget;
using HelpingHands.API;
using HelpingHands.Utils;
using System;

namespace HelpingHands
{
    [Activity(Label = "HelpingHands", Theme = "@style/AppTheme", MainLauncher = true)]    
    public class LoginActivity : Activity
    {
        EditText PasswordBox;
        EditText UsernameBox;
        Button LoginButton;
        TextView RegisterLink;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_login);

            PasswordBox = FindViewById<EditText>(Resource.Id.PasswordBox);
            UsernameBox = FindViewById<EditText>(Resource.Id.UsernameBox);
            LoginButton = FindViewById<Button>(Resource.Id.LoginButton);
            RegisterLink = FindViewById<TextView>(Resource.Id.RegisterLink);
            RegisterLink.PaintFlags |= PaintFlags.UnderlineText;

            LoginButton.Click += LoginButton_Clicked;
            RegisterLink.Click += RegisterLink_Click;
        }

        private void RegisterLink_Click(object sender, EventArgs e)
        {
            Intent intent = new Intent(this, typeof(RegisterVoluntarActivity));
            StartActivity(intent);
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
                AppSession.UserData = userInfo;

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