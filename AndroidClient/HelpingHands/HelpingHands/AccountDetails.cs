<<<<<<< HEAD
using Android.App;
=======
﻿using Android.App;
>>>>>>> 8a11c750ca4e083d89da77cabb6b4c84a545d238
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
<<<<<<< HEAD
using Google.Android.Material.BottomNavigation;
using HelpingHands.Adapters;
using HelpingHands.API;
using HelpingHands.Data;
=======
using HelpingHands.Adapters;
using HelpingHands.API;
>>>>>>> 8a11c750ca4e083d89da77cabb6b4c84a545d238
using HelpingHands.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
<<<<<<< HEAD
using static Android.App.DatePickerDialog;

namespace HelpingHands
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme")]
    public class AccountDetails : Activity
    {
        TextView Prenume;
        TextView Nume;
        TextView Email;
        TextView XpPct;
        Button ApplyForSponsorButton;
        Spinner InterestBox;

        InterestAdapter InterestAdapter;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.account_main);

            Prenume = FindViewById<TextView>(Resource.Id.Prenume);
            Nume = FindViewById<TextView>(Resource.Id.Nume);
            Email = FindViewById<TextView>(Resource.Id.Email);
            XpPct = FindViewById<TextView>(Resource.Id.XpPct);
            InterestBox = FindViewById<Spinner>(Resource.Id.InterestBox);

            ApplyForSponsorButton = FindViewById<Button>(Resource.Id.ApplyForSponsorButton);

            ApplyForSponsorButton.Click += ApplyForSponsorButton_Click;
=======

namespace HelpingHands
{
    public partial class MainVoluntarActivity
    {
        GridLayout ProfileView;

        TextView AccPrenume;
        TextView AccNume;
        TextView AccEmail;
        TextView AccXpPct;
        Button AccApplyForSponsorButton;
        Spinner AccInterestBox;        

        protected void OnCreateAccountPage()
        {
            ProfileView = FindViewById<GridLayout>(Resource.Id.ProfileView);

            AccPrenume = FindViewById<TextView>(Resource.Id.AccPrenume);
            AccNume = FindViewById<TextView>(Resource.Id.AccNume);
            AccEmail = FindViewById<TextView>(Resource.Id.AccEmail);
            AccXpPct = FindViewById<TextView>(Resource.Id.AccXpPct);
            AccInterestBox = FindViewById<Spinner>(Resource.Id.AccInterestBox);

            AccApplyForSponsorButton = FindViewById<Button>(Resource.Id.AccApplyForSponsorButton);

            AccApplyForSponsorButton.Click += ApplyForSponsorButton_Click;

            ProfileView.Visibility = ViewStates.Gone;
>>>>>>> 8a11c750ca4e083d89da77cabb6b4c84a545d238

            Task.Run(Load);
        }

        async void Load()
        {
            try
            {
                RunOnUiThread(async () =>
                {
                    Console.WriteLine("Account Profile Details");
<<<<<<< HEAD
                    Nume.Text = Nume.Text + " : " +  AppSession.UserData.User.Nume;
                    Email.Text = Email.Text + "     : " + AppSession.UserData.User.Email;
                    Prenume.Text = Prenume.Text + " : " + AppSession.UserData.User.Prenume;
                    XpPct.Text = XpPct.Text + "  : " + AppSession.UserData.User.XpPoints;
                });
                InterestAdapter = new InterestAdapter(this, (await API.Client.GetVoluntarInterests(AppSession.UserId)).ToList());
                RunOnUiThread(() => InterestBox.Adapter = InterestAdapter);
=======
                    AccNume.Text =AppSession.UserData.User.Nume;
                    AccEmail.Text = AppSession.UserData.User.Email;
                    AccPrenume.Text = AppSession.UserData.User.Prenume;
                    AccXpPct.Text = AppSession.UserData.User.XpPoints.ToString();
                });
                var interestAdapter = new InterestAdapter(this, (await API.Client.GetVoluntarInterests(AppSession.UserId)).ToList());
                RunOnUiThread(() => AccInterestBox.Adapter = interestAdapter);
>>>>>>> 8a11c750ca4e083d89da77cabb6b4c84a545d238
            }
            catch (Exception e)
            {
                await MessageBox.Alert(this, $"FAILED ACCOUNT PAGE\n\n{e.Message}", "Error");
            }
        }

        private void ApplyForSponsorButton_Click(object sender, EventArgs e)
        {
            throw new NotImplementedException();
        }
    }
}