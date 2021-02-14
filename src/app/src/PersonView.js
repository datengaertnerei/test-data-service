import React from 'react';

class PersonView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            error: null,
            isLoaded: false,
            email: null,
            givenName: null,
            familyName: null,
            birthDate: null,
            gender: null,
            taxId: null,
            addressLocality: null,
            postalCode: null,
            streetAddress: null,
            houseNumber: null,
            mobile: null,
            landline: null,
            ccType: null,
            ccNumber: null,
            ccNumber: null,
            bankAccount: null
        };
    }

    componentDidMount() {
        fetch("/api/v1/bundles")
            .then(res => res.json())
            .then(
                (result) => {
                    this.setState({
                        isLoaded: true,
                        email: result[0].person.email,
                        givenName: result[0].person.givenName,
                        familyName: result[0].person.familyName,
                        birthDate: result[0].person.birthDate,
                        gender: result[0].person.gender,
                        taxId: result[0].person.taxId,
                        addressLocality: result[0].person.address.addressLocality,
                        postalCode: result[0].person.address.postalCode,
                        streetAddress: result[0].person.address.streetAddress,
                        houseNumber: result[0].person.address.houseNumber,
                        mobile: result[0].mobile.phoneNumber,
                        landline: result[0].landline.phoneNumber,
                        ccType: result[0].creditCard.type,
                        ccNumber: result[0].creditCard.number,
                        ccCVC: result[0].creditCard.cvc,
                        bankAccount: result[0].bankAccount.iban
                    });
                },
                // Note: it's important to handle errors here
                // instead of a catch() block so that we don't swallow
                // exceptions from actual bugs in components.
                (error) => {
                    this.setState({
                        isLoaded: true,
                        error
                    });
                }
            )
    }

    render() {
        const { error,
            isLoaded,
            email,
            givenName,
            familyName,
            birthDate,
            taxId,
            addressLocality,
            postalCode,
            streetAddress,
            houseNumber,
            mobile,
            landline,
            ccType,
            ccNumber,
            ccCVC,
            bankAccount,
            gender } = this.state;
        if (error) {
            return <div>Error: {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading...</div>;
        } else {
            return (
                <div>
                    <p>{givenName} {familyName}</p>
                    <p>{streetAddress} {houseNumber}</p>
                    <p>{postalCode}  {addressLocality}</p>
                    <p>{birthDate} - ID: {taxId}</p>
                    <p>{landline}</p>
                    <p>{mobile}</p>
                    <p>{email}</p>
                    <p>{ccType} {ccNumber}</p>
                    <p>{bankAccount}</p>
                    <div><img src="/api/v1/avatar?gender={gender}" /></div>
                </div>
            );
        }
    }
}

export default PersonView;