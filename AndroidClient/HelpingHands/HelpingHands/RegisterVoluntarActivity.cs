using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using HelpingHands.API;
using HelpingHands.Data;
using HelpingHands.UI;
using HelpingHands.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HelpingHands
{
    [Activity(Label = "HelpingHands Register", Theme = "@style/AppTheme")]
    [UI.Layout("activity_register_voluntar")]
    public class RegisterVoluntarActivity : AutoLoadActivity
    {
        [Control] EditText UsernameBox;
        [Control] EditText Pass1Box;
        [Control] EditText Pass2Box;
        [Control] EditText EmailBox;
        [Control] EditText NumeBox;
        [Control] EditText PrenumeBox;
        [Control] Button RegisterButton;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            RegisterButton.Click += RegisterButton_Click;
        }

        private async void RegisterButton_Click(object sender, EventArgs e)
        {
            try
            {
                var username = UsernameBox.Text;
                var pass1 = Pass1Box.Text;
                var pass2 = Pass2Box.Text;
                var email = EmailBox.Text;
                var nume = NumeBox.Text;
                var prenume = PrenumeBox.Text;

                if (pass1 != pass2)
                {
                    await MessageBox.Alert(this, "Password confirmation failed", "Error");
                    return;
                }

                var user = await Client.Register(username, pass1, email, nume, prenume);
                await MessageBox.Alert(this, "Register successful");
                StartActivity(new Intent(this, typeof(LoginActivity)));
            }
            catch (Exception ex)
            {
                await MessageBox.Alert(this, ex.Message, "Error");
            }
        }
    }
}