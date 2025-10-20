var permalink = '';
var enableUTM = false;
const utmLimit = 78;
const toggleSaveSetting = function (on) {

    if (undefined == on)
        on = false;

    const saveBtn = document.querySelector('#save_settings');
    const saveBtnConfig = document.querySelector('#save_settings_config');

    if (typeof (saveBtn) != "undefined" && saveBtn !== null) {
        saveBtn.setAttribute('disabled', 'disabled');
        saveBtn.classList.remove('ecp-red-button');

        if (on == true) {

            // saveBtn.classList.add = 'ecp-green-button';
            saveBtn.removeAttribute('disabled');
            saveBtn.classList.add('ecp-red-button');
            // document.querySelector('#save_settings').classList.add = 'ecp-green-button';
        }
    }

    if (typeof (saveBtnConfig) != "undefined" && saveBtnConfig !== null) {
        saveBtnConfig.setAttribute('disabled', 'disabled');
        saveBtnConfig.classList.remove('ecp-red-button');

        if (on == true) {

            // saveBtn.classList.add = 'ecp-green-button';
            saveBtnConfig.removeAttribute('disabled');
            saveBtnConfig.classList.add('ecp-red-button');
            // document.querySelector('#save_settings').classList.add = 'ecp-green-button';
        }
    }

};

function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return typeof sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
    return false;
}

// Update values of saved/hidden setting fields
const updateSavedFields = function () {

    const settingFields = document.querySelectorAll('.field-setting');

    for (const field of settingFields) {
        const savedField = document.querySelector(`input[name='${field.dataset.ref}']`);
        if ( savedField ) {
            savedField.value = field.value;
        }
    }

    // update posting schedule button
    jQuery('.ecp-activate-button').removeClass('ecp-hidden');
    jQuery('span.active').addClass('ecp-hidden');
    const newValField = document.querySelector('input[name="ecp_posting_schedule"]:checked');

    if (newValField) {
        const activateButton = document.querySelector('button[data-radio="' + newValField.className + '"]');
        const siblingElement = activateButton.nextElementSibling;
        activateButton.classList.add('ecp-hidden');
        siblingElement.classList.remove('ecp-hidden');
    }

};

jQuery(function ($) {

    /**
     * Return all URLs/links found in the post content
     * @param {string} content
     */
    const getUrls = function (content) {
        const regex = /(https?:\/\/(?:www\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\.[^\s]{2,}|www\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\.[^\s]{2,}|https?:\/\/(?:www\.|(?!www))[a-zA-Z0-9]+\.[^\s]{2,}|www\.[a-zA-Z0-9]+\.[^\s]{2,})/g;
        return content.match(regex);
    };

    /**
     * JavaScript equivalent to PHP's ucwords
     */
    const ucwords = function (str) {
        return (str + '').replace(/^([a-z])|\s+([a-z])/g, function ($1) {
            return $1.toUpperCase();
        });
    };

    /**
     * Update the counter/input limit shown as the user types into the post content field
     * @param {event} e
     */
    const updateCounter = function (e) {
        var current_status_text = $('.ecp-add-post').attr('data-status-text');
        var current_status_image = $('.ecp-add-post').attr('data-status-image');
        const content = this.value;

        if (content.length > 0) {
            $('.ecp-cancel-without-popup').addClass('ecp-hidden');
            $('.ecp-cancel-with-popup').removeClass('ecp-hidden');
        } else {
            if ($('.upload-image-new').hasClass('hidden')) {
                $('.ecp-cancel-without-popup').removeClass('ecp-hidden');
                $('.ecp-cancel-with-popup').addClass('ecp-hidden');
            }
        }

        const limit = parseInt(this.dataset.counter);
        var url_length = parseInt(this.dataset.urllength);
        var is_image = $(this).parents('#ecp-profile-posts-block').find('.is_image').val();
        var network = $(this).attr('data-network');
        if (is_image == 1) {
            if (network != 'twitter') {
                var count = limit - parseInt(this.value.length) - parseInt(url_length);
            } else {
                var countUrl = urlify(this.value);
                var content_length_new = replaceUrl(this.value);

                var count = limit - content_length_new - countUrl * 23;
            }
        } else {
            if (network != 'twitter') {
                var count = limit - parseInt(this.value.length);
            } else {
                var countUrl = urlify(this.value);
                var content_length_new = replaceUrl(this.value);

                var count = limit - content_length_new - countUrl * 23;
            }
        }
        // update text counter based on enabled UTM setting
        // let deductCount = permalink.length;
        // if (enableUTM)
        //     deductCount += utmLimit;
        //
        // if ('twitter' != this.dataset.network) {
        //
        //     let imageInput = $(this).parents('.ecp-post-fields-container').find('.ecp-image-actions input.ecp-post-image');
        //
        //     if (imageInput.val() != '') {
        //         limit -= deductCount;
        //     }
        // }

        // let count = limit - parseInt(this.value.length);
        const links = getUrls(content);

        // if network is twitter, there are special rules to how character limit is implemented.
        // links no matter how long will always counts as 23 chars maximum
        // if ('twitter-profile' == this.dataset.network && links != undefined && links.length > 0) {

        //     // first get content length without URLs in the text
        //     let contentCopy = content;
        //     for (let i of links) {
        //         contentCopy = contentCopy.replace(i, '');
        //     }

        //     // second get lengths of each link, then add them
        //     let urlLengths = links.map( link => link.length > 23 ? 23 : link.length);
        //     let linkLengths = urlLengths.reduce( (a, b) => a + b);
        //     linkLengths += 1; // + the space before the link

        //     // deduct URL-less content length + total URL lengths from character limit for Twitter (280 max chars only)
        //     count = limit - (parseInt(contentCopy.length) + parseInt(linkLengths));
        // }

        //  save current content length to textarea attrib
        this.dataset.length = parseInt(count);

        // update the text count being shown
        this.previousElementSibling.querySelector('.counterNumber').innerText = count;
        this.closest('.ecp-new-data-row ').querySelector('.ecp-posttext-info .post-limit').innerHTML = count;

        if (count > 0 && count <= limit) {
            this.classList.remove('ecp-input-error');
            $('.post-limit').removeAttr('style');
            $('.ecp-countdown').removeAttr('style');
            // $('#ecp_custom_message').removeAttr('style');
            if (current_status_text == '0') {
                $('.ecp-add-post').attr('data-status-text', '1');
                if (current_status_image == 1) {
                    $('.ecp-add-post').attr('disabled', false);
                }
            }
        } else if (count < 0) {
            $('.post-limit').css({ 'color': '#fb5654', 'font-weight': 'bold' });
            $(this).parents('.ecp-input').find('.ecp-countdown').css('background-color', '#fb5654');
            $('.ecp-add-post').attr('data-status-text', '0');
            $('.ecp-add-post').attr('disabled', true);
        } else {
            $('.post-limit').removeAttr('style');
            $('.ecp-countdown').removeAttr('style');
            if (current_status_text == '0') {
                $('.ecp-add-post').attr('data-status-text', '1');
                if (current_status_image == 1) {
                    $('.ecp-add-post').attr('disabled', false);
                }
            }
        }

    };

    const updateCounterShare = function (e) {
        var current_status_text = this.closest('.ecp-content-share-network').querySelector('.ecp-save-post-share').getAttribute('data-status-text');
        var current_status_image = this.closest('.ecp-content-share-network').querySelector('.ecp-save-post-share').getAttribute('data-status-image');

        const limit = parseInt(this.dataset.counter);
        var url_length = parseInt(this.dataset.urllength);
        var is_image = $(this).parents('.ecp-content-share-network').find('.is_image').val();
        var network = this.getAttribute('data-network');
        if (is_image == 1) {
            if (network != 'twitter') {
                var count = limit - parseInt(this.value.length) - parseInt(url_length);
            } else {
                var countUrl = urlify(this.value);
                var content_length_new = replaceUrl(this.value);

                var count = limit - content_length_new - countUrl * 23;
            }
        } else {
            if (network != 'twitter') {
                var count = limit - parseInt(this.value.length);
            } else {
                var countUrl = urlify(this.value);
                var content_length_new = replaceUrl(this.value);

                var count = limit - content_length_new - countUrl * 23;
            }
        }

        //  save current content length to textarea attrib
        this.dataset.length = parseInt(count);

        // update the text count being shown
        this.previousElementSibling.querySelector('.counterNumberShare').innerText = count;
        this.closest('.ecp-new-data-row ').querySelector('.ecp-posttext-info .post-limit-share').innerHTML = count;
        if (count > 0 && count <= limit) {
            this.classList.remove('ecp-input-error');
            this.closest('.ecp-content-share-network').querySelector('.post-limit-share').removeAttribute('style');
            this.closest('.ecp-content-share-network').querySelector('.ecp-countdown').removeAttribute('style');
            if (current_status_text == '0') {
                this.closest('.ecp-content-share-network').querySelector('.ecp-save-post-share').setAttribute('data-status-text', '1');
                if (current_status_image == 1) {
                    this.closest('.ecp-content-share-network').querySelector('.ecp-save-post-share').removeAttribute("disabled");
                }
            }
        } else if (count < 0) {
            this.closest('.ecp-content-share-network').querySelector('.post-limit-share').style.cssText += 'color: #fb5654; font-weight: bold';
            this.closest('.ecp-content-share-network').querySelector('.ecp-countdown').style.cssText += 'background-color: #fb5654';
            this.closest('.ecp-content-share-network').querySelector('.ecp-save-post-share').setAttribute('data-status-text', '0');
            this.closest('.ecp-content-share-network').querySelector('.ecp-save-post-share').setAttribute('disabled', true);
        } else {
            this.closest('.ecp-content-share-network').querySelector('.post-limit-share').removeAttribute('style');
            this.closest('.ecp-content-share-network').querySelector('.ecp-countdown').removeAttribute('style');
            if (current_status_text == '0') {
                this.closest('.ecp-content-share-network').querySelector('.ecp-save-post-share').setAttribute('data-status-text', '1');
                if (current_status_image == 1) {
                    this.closest('.ecp-content-share-network').querySelector('.ecp-save-post-share').removeAttribute("disabled");
                }
            }
        }

    };

    function urlify(text) {
        var urlRegex = new RegExp(/(\s+(https?:\/\/|www\.)[a-z][a-z0-9\-\.]*[a-z]([^\s]*))/, "gim")

        const text2 = ' ' + text + ' ';
        if (text2.match(urlRegex)) {
            var countUrl = text2.match(urlRegex).length;
        } else {
            var countUrl = 0;
        }

        return countUrl;
    }

    function urlify2(text) {
        var urlRegex = new RegExp(/(\s+(https?:\/\/|www\.)[a-z][a-z0-9\-\.]*[a-z]([^\s]*))/, "gim");
        return text.replace(urlRegex, function (url) {
            return ' <a class="ecp-library-direct-link" target="_blank" rel="nofollow" href="' + url.trim() + '">' + url.trim() + '</a>';
        })
    }

    function replaceUrl(text) {
        var urlRegex = /(\s+(https?:\/\/|www\.)[a-z][a-z0-9\-\.]*[a-z]([^\s]*))/gim;


        var text2 = ' ' + text + ' ';
        if (text2.match(urlRegex)) {
            var length_url = text2.match(urlRegex).reduce((a, x) => a + x.trim().length, 0);
        } else {
            length_url = 0;
        }

        return text.length - length_url;
        // or alternatively
        // return text.replace(urlRegex, '<a href="$1">$1</a>')
    }

    /**
     * Convert a UNIX timestamp into a human-readable date format
     * @param {int} timestamp
     */
    const convertTimestamp = function (timestamp) {
        // add custom 3 letter shortcodes for months to Date object
        Date.shortMonths = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

        const dateTime = new Date(timestamp * 1000);
        // const hours = dateTime.getHours();
        const minutes = "0" + dateTime.getMinutes();
        const seconds = "0" + dateTime.getSeconds();

        // display time in MMM DD, YYYY hh:mm:ss format
        const formattedTime = [
            Date.shortMonths[dateTime.getMonth()], // 3 letter month code
            ' ',
            dateTime.getDate(),
            ', ',
            dateTime.getFullYear(),
            ' ',
            dateTime.getHours(),
            ':',
            minutes.substr(-2),
            ':',
            seconds.substr(-2)
        ];

        return formattedTime.join('');
        // const formattedTime = Date.shortMonths[date.getMonth()] + ' ' + date.getDate() + ', ' + date.getFullYear() + ' ' + date.getHours() + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
        // return formattedTime;
    };

    // delete the corresponding ECP post
    const deleteSavedPost = function (e) {

        this.closest('.ecp-post-saved-template').classList.add('loading');
        var category_id = this.closest('.ecp-post-saved-template').querySelector('.saved-category-id').value;

        const ecpPostId = this.closest('.ecp-post-saved-template').querySelector('.ecp-post-id').value;
        // const postId = document.querySelector('#ecp-post-ref').value;
        let postId = 0;

        const postRefInput = document.querySelector('#ecp-post-ref');
        if (postRefInput != null)
            postId = postRefInput.value;
        else
            postId = this.closest('.ecp-post-saved-template').querySelector('.ecp-post-ref').value;

        // set ajax data
        const data = {
            'action': 'delete_network_post',
            'post_ref': postId,
            'ecp_post_id': ecpPostId,
            'security': ajaxSettings.deleteNetworkPostNonce
        };

        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {

                if (response.success) {
                    jQuery('#ecp_post_' + ecpPostId).slideUp();
                    setTimeout(function () {
                        var show_no_post = true;
                        $('#ecp-post-list').find('.ecp-post-saved-template').each(function (e) {
                            if (!($(this).css('display') == 'none')) {
                                show_no_post = false;
                            }
                        });
                        if (show_no_post == true) {
                            $('.ecp-no-content-list').removeClass('hidden');
                        }
                    }, 1000);
                    var post_number_old = $('#category_box_' + category_id).find('.posts_number_all').text();
                    var post_number = parseInt(post_number_old) - 1;
                    $('#category_box_' + category_id).find('.posts_number_all').text(post_number);
                } else {
                    jQuery('#ecp_post_' + ecpPostId).removeClass('loading');
                }
                return;
            });
        return false;
    };

    const saveNetworkPost = function (e) {

        e.preventDefault();

        // prevent double click
        this.disabled = true;

        const container = $(this).parents('.ecp-post-fields-container');
        const profileData = container.find('.ecp-profile-data');
        const postText = container.find('.ecp-service-post-text');
        const postMedia = container.find('.ecp-post-image');
        const postUrl = container.find('.url-plugin');
        const category_id = container.find('.ecp_category').val();
        const postId = document.querySelector('#ecp-post-ref').value;
        var category_name = container.find('.ecp_category option:selected').text();
        var dataService = $('.ecp-service').val();

        var ecp_add_post_nonce = $(this).attr('data-ecp-add-post-nonce');
        //Get the Google business fields from php.
        var postType = container.find('input[type=radio][name=ecp-google-business-profile]:checked').val();
        var buttonNew = container.find('.ecp-google-business-profile-post-button-whats-new option:selected').text();
        var buttonLinkNew = container.find('.ecp-google-business-profile-post-button-new-link').val();
        var eventTitle = container.find('.ecp-google-business-event-title').val();
        var eventDate = container.find('.ecp-google-business-event-date').val();
        var eventAddTime = container.find('.ecp-checkbox-toggle').attr('data-status');
        var eventTime = container.find('.ecp-google-business-event-time').val();
        var eventButton = container.find('.ecp-google-business-profile-post-button-event option:selected').text();
        var eventButtonLink = container.find('.ecp-google-business-event-button-link').val();
        var offerTitle = container.find('.ecp-google-business-offer-title').val();
        var offerDate = container.find('.ecp-google-business-offer-date').val();
        var offerAddTime = container.find('.ecp-checkbox-toggle').attr('data-status');
        var offerTime = container.find('.ecp-google-business-offer-time').val();
        var offerButton = container.find('.ecp-google-business-offer-coupon').val();
        var offerButtonLink = container.find('.ecp-google-business-offer-link').val();
        var offerTerm = container.find('.ecp-google-business-offer-terms').val();

        var social_name = container.find('.ecp-social-name-add-post').val();

        var ecp_category_search = $('.ecp_category_sort').val();
        var ecp_content_search = $('.ecp_content_search').val();

        var in_library = $('.in-library').val();

        var limit = document.querySelector('.ecp-posttext-texts .ecp-posttext.' + dataService).dataset.limit;
        // console.log('limit', limit);
        postText.removeClass('ecp-input-error');
        // if (postText.val().length <= 0) {
        if (postText.attr('data-length') == undefined || parseInt(postText.attr('data-length')) < 0 || postText.val().length <= 0 ) {
            postText.addClass('ecp-input-error');
            this.disabled = false;
            return false;
        }

        // Validation for the Google business required fields.
        if( postType ) {
            if('call-to-action' == postType && buttonLinkNew.length <= 0 ) {
                container.find('.ecp-google-business-profile-post-text').addClass('ecp-input-error');
                this.disabled = false;
                return false;
            }
            else if( 'event' == postType && eventTitle.length <= 0 || eventButtonLink.length <= 0 ) {
                container.find('.ecp-google-business-profile-post-text').addClass('ecp-input-error');
                if( !eventDate ) {
                    container.find('.ecp-google-business-profile-post-date').addClass('ecp-input-error');
                }
                this.disabled = false;
                return false;
            }
            else if( 'offer' == postType &&  offerTitle.length <= 0 ) {
                container.find('.ecp-google-business-profile-post-text').addClass('ecp-input-error');
                if( !offerDate ) {
                    container.find('.ecp-google-business-profile-post-date').addClass('ecp-input-error');
                }
                this.disabled = false;
                return false;
            }
        }

        // validation for the image field is require for the google business,instagram and tiktok.
        if ( !postMedia.val() ) {
            if( 'googlebusiness' == postText.attr('data-network') || 'instagram' == postText.attr('data-network') || 'tiktok' == postText.attr('data-network')) {
                container.find('.ecp-label-btn').addClass('ecp-input-error');
                this.disabled = false;
                return false;
            }
        }

        // do not continue if post ref ID is missing, or no post content text is found
        if ((postId <= 0 && postId != '') || document.querySelectorAll('.ecp-input-error').length > 0) {
            return false;
        }


        // set ajax data
        const data = {
            'action': 'create_network_post',
            'post_ref': postId,
            'post_text': postText.val(),
            'postType':postType,
            'buttonNew':buttonNew,
            'buttonLinkNew':buttonLinkNew,
            'eventTitle':eventTitle,
            'eventDate':eventDate,
            'eventAddTime':eventAddTime,
            'eventTime':eventTime,
            'eventButton':eventButton,
            'eventButtonLink':eventButtonLink,
            'offerTitle':offerTitle,
            'offerDate':offerDate,
            'offerAddTime':offerAddTime,
            'offerTime':offerTime,
            'offerButton':offerButton,
            'offerButtonLink':offerButtonLink,
            'offerTerm':offerTerm,
            'post_media': postMedia.val(),
            'network_profile': postText.attr('data-profile'),
            'network_service': postText.attr('data-network'),
            'category_id': category_id,
            'ecp_add_post_nonce': ecp_add_post_nonce
        };

        const networkName = (ucwords(postText.attr('data-network'))).toLowerCase();

        const saveLoader = document.querySelector('.ecp-loading-saving');
        saveLoader.innerText = document.querySelector('label.saving-post').innerText;

        saveLoader.classList.add('show');

        $('.saving-network-name').text(networkName);

        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {
                if (response.success) {
                    $('.upload-image-new').addClass('hidden');
                    $('.upload-image-old').removeClass('hidden');
                    if( 'googlebusiness' == networkName || 'instagram' == networkName ) {
                        var defaultImage = postUrl.val() + '/admin/img/ecp-no-visual-attached-mandatory.png';
                    } else if ( 'tiktok' == networkName ) {
                        var defaultImage = postUrl.val() + '/admin/img/ecp-no-video-attached-mandatory.png';
                    }
                    else {
                        var defaultImage = postUrl.val() + '/admin/img/ecp-no-visual-attached.png';
                    }

                    const savedPost = $('.ecp-post-saved-base .ecp-post-saved-template').clone();

                    var url_length = savedPost.find('.url-length').val();

                    savedPost.find('.ecp-post-last-shared span.network_name').html(networkName);
                    savedPost.find('span.category-name').html(category_name);
                    savedPost.find('.ecp-post-last-shared span.post_timestamp').html(convertTimestamp(response.data.timestamp));
                    savedPost.find('.ecp-post-id').val(response.data.ecp_post_id);
                    savedPost.find('.saved-category-id').val(category_id);
                    savedPost.find('.ecp-post-ref').val(postId);
                    savedPost.attr('id', 'ecp_post_' + response.data.ecp_post_id);
                    savedPost.find('.change-post-in-library').attr('id', 'add-row-' + response.data.ecp_post_id);
                    savedPost.find('.change-post-in-library').attr('data-id', response.data.ecp_post_id);
                    savedPost.find('.ecp-category-library').val(category_id);
                    savedPost.find('select option').each(function () {
                        if ($(this).val() == category_id)
                            $(this).attr("selected", "selected");
                    });
                    savedPost.find('.cancel-change').attr('data-id', response.data.ecp_post_id);
                    savedPost.find('.ecp-service-post-text-library').attr('data-network', networkName);
                    savedPost.find('.ecp-service-post-text-library').addClass('text-content-' + response.data.ecp_post_id);
                    savedPost.find('.ecp-service-post-text-library').attr('data-counter', limit);
                    savedPost.find('.ecp-service-post-text-library').attr('data-length', (limit - postText.val().length - url_length));
                    savedPost.find('.ecp-service-post-text-library').attr('data-id', response.data.ecp_post_id);
                    savedPost.find('.ecp-service-post-text-library').html(postText.val());
                    savedPost.find('.counterNumber').html(limit - postText.val().length - url_length);
                    savedPost.find('label').attr('for', 'wp_uploadfile_new_' + response.data.ecp_post_id);
                    savedPost.find('label').attr('data-id', response.data.ecp_post_id);
                    savedPost.find('.ecp-fileinput').attr('data-id', response.data.ecp_post_id);
                    savedPost.find('.ecp-fileinput').attr('id', 'wp_uploadfile_new_' + response.data.ecp_post_id);
                    savedPost.find('.ecp-fileinput').attr('id', 'wp_uploadfile_new_' + response.data.ecp_post_id);

                    if (!postMedia.val()) {
                        savedPost.find('.ecp-post-image-display').attr('src', defaultImage);
                        savedPost.find('.is_image').val(0);
                    } else {
                        if('tiktok' == networkName ) {
                            savedPost.find('video.ecp-post-image-display.ecp-post-video-display').addClass('ecp-post-video-active');
                            savedPost.find('img.ecp-post-image-display').hide();
                        }
                        savedPost.find('.ecp-post-image-display').attr('src', postMedia.val());
                        savedPost.find('.is_image').val(1);
                    }

                    // only replace the default image if a visual is attached
                    // if (postMedia.val().length > 0)
                    //     savedPost.find('.ecp-post-image').attr('src', postMedia.val());

                    savedPost.find('.ecp-dropdown').find('.ecp-share-now-action').attr('data-id', response.data.ecp_post_id);
                    savedPost.find('.ecp-dropdown').find('.ecp-share-now-action').attr('data-content', postText.val());
                    savedPost.find('.ecp-dropdown').find('.ecp-share-now-action').attr('data-social-name', social_name);
                    if (!postMedia.val()) {
                        savedPost.find('.ecp-dropdown').find('.ecp-share-now-action').attr('data-image', '');
                    } else {
                        savedPost.find('.ecp-dropdown').find('.ecp-share-now-action').attr('data-image', postMedia.val());
                    }

                    savedPost.find('.ecp-dropdown').find('.ecp-add-queue-action').attr('data-id', response.data.ecp_post_id);
                    savedPost.find('.ecp-dropdown').find('.ecp-add-queue-action').attr('data-content', postText.val());
                    savedPost.find('.ecp-dropdown').find('.ecp-add-queue-action').attr('data-social-name', social_name);
                    if (!postMedia.val()) {
                        savedPost.find('.ecp-dropdown').find('.ecp-add-queue-action').attr('data-image', '');
                        savedPost.find('.ecp-image-actions').find('.ecp-image-delete-add-media-saved').addClass('hidden');
                    } else {
                        savedPost.find('.ecp-dropdown').find('.ecp-share-now-action').attr('data-image', postMedia.val());
                    }


                    savedPost.find('.ecp-dropdown').find('ecp-share-now-action').attr('data-id', response.data.ecp_post_id);
                    savedPost.find('.ecp-dropdown').find('ecp-share-now-action').attr('data-content', postText.val());
                    savedPost.find('.ecp-dropdown').find('ecp-share-now-action').attr('data-image', postText.val());

                    savedPost.find('.ecp-profile-circle-small').attr('ng-src', profileData.attr('data-avatar'));
                    savedPost.find('.ecp-profile-circle-small').attr('src', profileData.attr('data-avatar'));
                    savedPost.find('.ecp-social-network-icon-small').removeClass('ecp-twitter');
                    savedPost.find('.ecp-social-network-icon-small').addClass("ecp-" + profileData.attr('data-network'));
                    savedPost.find('.ecp-post-text .ecp-post-content').html(
                        '<span class="ecp-post-content-show">' + urlify2(postText.val()) + '</span> <a rel="nofollow" class="ecp-library-wp-post-link" href="" target="_blank"></a>'
                    );
                    savedPost.find('.ecp-post-delete').bind('click', deleteSavedPost);

                    if (in_library == 1) {
                        var text = postText.val();
                        if (text.indexOf(ecp_content_search) != -1 && (category_id == ecp_category_search || ecp_content_search == 0)) {
                            savedPost.prependTo('#ecp-post-list');
                            $('.ecp-no-result-class').addClass('ecp-hidden');
                        }
                    } else {
                        savedPost.prependTo('#ecp-saved-posts-block');
                    }
                    if (networkName == 'instagram' || networkName == 'pinterest') {
                        savedPost.find('.ecp-image-delete-add-media-saved').addClass('hidden');
                    }

                    var post_number = $('#category_box_' + category_id).find('.posts_number_all').text();
                    post_number = parseInt(post_number) + 1;
                    $('#category_box_' + category_id).find('.posts_number_all').text(post_number);

                    container.find('.ecp-post-image').each(function () {
                        $(this).val("");
                    });

                    $('.ecp-fields-base').find('.ecp-post-image').val("");
                    document.querySelector('.ecp-network-container').classList.remove('ecp-hidden');
                    $('.ecp-network-container').each(function () {
                        $(this).removeClass('ecp-hidden');
                    });
                    document.querySelector('#ecp-profile-posts-block').innerHTML = '';

                    setTimeout(function () {
                        saveLoader.classList.add('ecp-loading-success');
                        saveLoader.innerText = document.querySelector('label.saving-post-success').innerText;
                    }, 500);

                    // document.querySelector('#ecp-saved-posts-block').appendChild(savedPost);
                } else {
                    saveLoader.innerText = document.querySelector('label.saving-post-error').innerText;
                }

                setTimeout(function () {
                    saveLoader.classList.remove('ecp-loading-success');
                    saveLoader.classList.remove('show');
                    saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                }, 2000);

                $('.ecp-no-content-list').addClass('hidden');
                this.disabled = false;
                return;

            });
    };

    /**
     * When click save in share immediately
     *
     * @param e
     * @returns {boolean}
     */
    const saveNetworkPostShare = function (e) {
        e.preventDefault();
        // prevent double click
        var _this = this;
        this.disabled = true;

        const container = $(this).parents('.ecp-content-share-network');
        const postText = container.find('.ecp-service-post-text');
        const postMedia = container.find('.ecp-post-image-share');
        const category_id = container.find('.ecp_category').val();
        var postId = $('#ecp-post-ref').val();
        if (!postId || typeof (postId) === "undefined") {
            postId = $('#post_ID').val();
        }

        let is_immediately = container.find('.is-immediately').val();
        let immediately_id = container.find('.ecp-immediately-post-id').val();
        let immediately_ref_id = container.find('.ecp-post-immediately-ref').val();
        var category_name = container.find('.ecp_category option:selected').text();
        var dataService = container.find('.ecp-service').val();
        var save_library = 0;
        if (container.find('.save_library').is(":checked")) {
            save_library = 1;
        }

        postText.removeClass('ecp-input-error');
        // if (postText.val().length <= 0) {
        if (postText.attr('data-length') == undefined || parseInt(postText.attr('data-length')) < 0 || postText.val().length <= 0) {
            postText.addClass('ecp-input-error');
            this.disabled = false;
            return false;
        }

        // do not continue if post ref ID is missing, or no post content text is found
        if ((postId <= 0 && postId != '') || document.querySelectorAll('.ecp-input-error').length > 0) {
            return false;
        }

        // set ajax data
        const data = {
            'action': 'create_network_post_share',
            'post_ref': postId,
            'post_text': postText.val(),
            'post_media': postMedia.val(),
            'network_profile': postText.attr('data-profile'),
            'network_service': postText.attr('data-network'),
            'category_id': category_id,
            'save_library': save_library,
            'is_immediately': is_immediately,
            'immediately_id': immediately_id,
            'immediately_ref_id': immediately_ref_id
        };

        let networkName = (ucwords(postText.attr('data-network'))).toLowerCase();
        networkName = ucwords(networkName);

        const saveLoader = document.querySelector('.ecp-loading-saving');
        saveLoader.innerText = document.querySelector('label.saving-post-right').innerText;

        saveLoader.classList.add('show');

        $('.saving-network-name').text(networkName);

        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {
                if (response.success) {
                    container.find('.ecp-network-select').attr('data-is-immediately', 1);
                    container.find('.is-immediately').val(1);
                    container.find('.ecp-immediately-post-id').val(response.data.ecp_post_id);

                    setTimeout(function () {
                        saveLoader.classList.add('ecp-loading-success');
                        saveLoader.innerText = document.querySelector('label.saving-post-success-right').innerText;
                    }, 500);
                } else {
                    saveLoader.innerText = document.querySelector('label.saving-post-error').innerText;
                }

                setTimeout(function () {
                    saveLoader.classList.remove('ecp-loading-success');
                    saveLoader.classList.remove('show');
                    setTimeout(function () {
                        saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                    }, 2000);
                }, 2000);

                container.find('.ecp-content-box').addClass('hidden');
                container.find('.ecp-message-box').removeClass('hidden');
                container.find('.ecp-keep-message').addClass('hidden');
                container.find('.ecp-delete-message').removeClass('hidden');
                _this.disabled = false;
                return;

            });
    };

    $(document).on('click', '.ecp-delete-message-but', function (e) {
        e.preventDefault();
        // prevent double click
        var _this = $(this);
        // this.disabled = true;

        const container = $(this).parents('.ecp-content-share-network');
        var postId = $('#ecp-post-ref').val();
        if (!postId || typeof (postId) === "undefined") {
            postId = $('#post_ID').val();
        }

        let network_profile = _this.attr('data-profile');

        let immediately_id = $('#ecp-share-box-' + network_profile).find('.ecp-immediately-post-id').val();

        // do not continue if post ref ID is missing, or no post content text is found
        if ((postId <= 0 && postId != '') || document.querySelectorAll('.ecp-input-error').length > 0) {
            return false;
        }

        // set ajax data
        const data = {
            'action': 'delete_network_post_share',
            'security': ajaxSettings.deleteNetworkPostShareNonce,
            'post_ref': postId,
            'immediately_id': immediately_id,
            'network_profile': network_profile
        };

        let networkName = (ucwords(_this.attr('data-network'))).toLowerCase();
        networkName = ucwords(networkName);

        const saveLoader = document.querySelector('.ecp-loading-saving');
        saveLoader.innerText = document.querySelector('label.deleting-post-right').innerText;

        saveLoader.classList.add('show');

        $('.saving-network-name').text(networkName);

        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {
                if (response.success) {
                    $('#ecp-share-box-' + network_profile).find('.ecp-network-select').attr('data-is-immediately', 0);
                    $('#ecp-share-box-' + network_profile).find('.is-immediately').val(0);
                    $('#ecp-share-box-' + network_profile).find('.ecp-immediately-post-id').val("");
                    $('#ecp-share-box-' + network_profile).find('.ecp-service-post-text').val("");
                    $('#ecp-share-box-' + network_profile).find('.url-length').val("");
                    $('#ecp-share-box-' + network_profile).find('.is_image').val(0);
                    $('#ecp-share-box-' + network_profile).find('.upload-image-new-share').addClass('hidden');
                    $('#ecp-share-box-' + network_profile).find('.upload-image-old-share').removeClass('hidden');
                    $('#ecp-share-box-' + network_profile).find('.ecp-post-image-share').val('');

                    setTimeout(function () {
                        saveLoader.classList.add('ecp-loading-success');
                        saveLoader.innerText = document.querySelector('label.deleting-post-success-right').innerText;
                    }, 500);
                } else {
                    saveLoader.innerText = document.querySelector('label.deleting-post-error-right').innerText;
                }

                setTimeout(function () {
                    saveLoader.classList.remove('ecp-loading-success');
                    saveLoader.classList.remove('show');
                    setTimeout(function () {
                        saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                    }, 2000);
                }, 2000);

                $('#ecp-share-box-' + network_profile).find('.ecp-content-box').addClass('hidden');
                $('#ecp-share-box-' + network_profile).find('.ecp-message-box').addClass('hidden');
                $('#ecp-share-box-' + network_profile).find('.ecp-network-select').removeAttr('checked');
                // _this.disabled = false;
                return;

            });
    });

    // Save library check
    $(document).on('click', '.save_library', function (e) {
        const container = $(this).parents('.ecp-content-share-network');
        if ($(this).is(":checked")) {
            container.find('.ecp-category-share').removeClass('hidden');
        } else {
            container.find('.ecp-category-share').addClass('hidden');
        }
    });

    // Share immediately check
    $(document).on('click', '#is_share', function (e) {
        var is_check = 0;
        if ($(this).is(":checked")) {
            $(this).parents('.ecp-wrapper-right').find('.network-list').removeClass('hidden');
            $('.not_connect_buffer_text').removeClass('hidden');
            is_check = 1;
        } else {
            $('.not_connect_buffer_text').addClass('hidden');
            $(this).parents('.ecp-wrapper-right').find('.network-list').addClass('hidden');
        }

        var id = $('#post_ID').val();
        // Call ajax save status checked
        // set ajax data
        const data = {
            'action': 'is_share_status_save',
            'is_check': is_check,
            'id': id
        };
        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {

                if (response.success) {

                } else {
                    console.log('Error');
                }
            });
    });

    $(document).on('click', '.ecp-cancel-add-post', function () {
        $('.upload-image-new').addClass('hidden');
        $('.upload-image-old').removeClass('hidden');

        $(this).parents('.ecp-post-fields-container').find('.ecp-add-post').disabled = true;
        const postUrl = $(this).parents('.ecp-post-fields-container').find('.url-plugin');
        var defaultImage = postUrl.val() + '/admin/img/ecp-no-visual-attached.png'

        $(this).parents('.ecp-post-fields-container').find('.ecp-post-image').each(function () {
            $(this).val("");
        });

        $('.ecp-cancel-without-popup').removeClass('ecp-hidden');
        $('.ecp-cancel-with-popup').addClass('ecp-hidden');

        $('.ecp-fields-base').find('.ecp-post-image').val("");
        document.querySelector('.ecp-network-container').classList.remove('ecp-hidden');
        $('.ecp-network-container').each(function () {
            $(this).removeClass('ecp-hidden');
        });
        document.querySelector('#ecp-profile-posts-block').innerHTML = '';
    });

    $(document).on('click', '.ecp-save-post-cancel', function (e) {
        e.preventDefault();
        var contentParentBox = $(this).parents('.ecp-content-share-network');
        var is_immediately = $(this).attr('data-is-immediately');

        contentParentBox.find('.ecp-content-box').addClass('hidden');
        contentParentBox.find('.ecp-network-select').prop('checked', false);
        if (is_immediately) {
            contentParentBox.find('.ecp-message-box').addClass('hidden');
        }
    });

    $('.ecp-show-profiles').on('click', function () {
        $(this).next('.ecp-network-list').toggleClass('show');
    });

    $('.ecp-show-categories').on('click', function () {
        $(this).next('.ecp-categories-list').toggleClass('show');
    });

    $('.ecp-network-single').on('click', function () {

        const network = $(this);

        const formatted_service = network.attr('data-formatted-service');
        const dataService = network.attr('data-service');
        const datalength = network.attr('data-urllength');
        const dataNetwork = network.attr('data-network');
        const baseFields = $('.ecp-fields-base .ecp-fields-template');
        newPostBlock = baseFields.clone();

        switch (dataNetwork) {
            case 'instagram':
                // console.log('inside Instagram');
                newPostBlock.find('.ecp-posts-form-bottom-block span.ecp-input-title').text('Evergreen Content Share Image (mandatory)');
                break;
            case 'pinterest':
                newPostBlock.find('.ecp-posts-form-bottom-block span.ecp-input-title').text('Evergreen Content Share Image (mandatory)');
                break;
            case 'googlebusiness':
                newPostBlock.find('.ecp-google-business-main-wrap').addClass('ecp-google-active');
                newPostBlock.find('.ecp-posts-form-bottom-block span.ecp-input-title').text('Evergreen Content Share Image (mandatory)');
                break;
            case 'tiktok':
                newPostBlock.find('.ecp-posts-form-bottom-block span.ecp-input-title').text('Evergreen Content Share Video (mandatory)');
                break;
            default:
                newPostBlock.find('.ecp-posts-form-bottom-block span.ecp-input-title').text('Evergreen Content Share Image');
                break;
        }


        const ecpProfileData = newPostBlock.find('.ecp-profile-data');

        ecpProfileData.attr('data-avatar', network.attr('data-avatar'));
        ecpProfileData.attr('data-network', dataNetwork);

        // newPostBlock.find('.ecp-service-post-text').attr('data-counter', network.attr('data-service'));

        let servicePrefix = 'facebook-group';
        if (dataService == '')
            dataService = servicePrefix;

        const postTextInfo = document.querySelector('.ecp-posttext-texts .ecp-posttext.' + dataService);
        const postImageInfo = document.querySelector('.ecp-postimage-texts .ecp-postimage.' + dataService);

        // this was supposed to replace the @username value in the twitter profile infotext below the post form textarea
        // if ('twitter-profile' == dataService)
        //     postTextInfo.querySelector('.twitter-username').innerText = network.attr('data-username');

        newPostBlock.find('.counterNumber').text(postTextInfo.dataset.limit);
        newPostBlock.find('.ecp-posttext-info').html(postTextInfo.innerHTML);
        newPostBlock.find('.ecp-posttext-info').find('.post-limit').html(postTextInfo.dataset.limit);
        newPostBlock.find('.ecp-postimage-info').html(postImageInfo.innerHTML);
        newPostBlock.find('.ecp-post-image').attr('data-profile', network.attr('data-profile'));

        const ecpServicePostText = newPostBlock.find('.ecp-service-post-text');

        ecpServicePostText.attr('data-counter', postTextInfo.dataset.limit);
        ecpServicePostText.attr('data-length', postTextInfo.dataset.limit);

        ecpServicePostText.attr('data-service', dataService);
        ecpServicePostText.attr('data-network', dataNetwork);
        ecpServicePostText.attr('data-urllength', datalength);

        // if ('twitter-profile' != dataService)
        //     ecpServicePostText.attr('maxlength', postTextInfo.dataset.limit);

        ecpServicePostText.attr('data-profile', network.attr('data-profile'));

        // needed to re-bind the event as .on() is not working for cloned nodes
        ecpServicePostText.bind('keyup', updateCounter);

        $('#url-length').val(datalength);
        $('.ecp-post-saved-base').find('.url-length').val(datalength);
        $('.ecp-service').val(dataService);

        newPostBlock.find('.ecp-add-post').bind('click', saveNetworkPost);

        newPostBlock.find('.ecp-social-name-add-post').val(formatted_service);

        newPostBlock.appendTo('#ecp-profile-posts-block');

        // trigger hiding of add new post button
        document.querySelector('.ecp-show-profiles').click();
        document.querySelector('.ecp-network-container').classList.add('ecp-hidden');
        network.parents('.ecp-network-container').addClass('ecp-hidden');
        $('.ecp-add-post').attr('data-network', dataNetwork);
        $('.ecp-add-post').attr('data-status-text', '1');
        if (dataNetwork == 'instagram' || dataNetwork == 'pinterest') {
            $('.ecp-add-post').attr('data-status-image', '0');
            $('.ecp-add-post').attr('disabled', true);
        } else {
            $('.ecp-add-post').attr('data-status-image', '1');
        }

        $('.ecp-network-list').removeClass('show');
    });

    $('.ecp-network-select').on('click', function () {
        let _this = $(this);
        let is_immediately = _this.attr('data-is-immediately');
        is_immediately = $.trim(is_immediately);
        let contentShareBox = _this.parents('.ecp-content-share-network');
        var is_check = 0;
        if (_this.is(':checked')) {
            is_check = 1;
            if (is_immediately == 1) {
                contentShareBox.find('.ecp-message-box').removeClass('hidden');
            } else {
                contentShareBox.find('.ecp-content-box').removeClass('hidden');
                contentShareBox.find('.ecp-message-box').addClass('hidden');
            }

        } else {
            contentShareBox.find('.ecp-content-box').addClass('hidden');
            contentShareBox.find('.ecp-message-box').addClass('hidden');
        }
        const network = $(this);

        var id = $('#post_ID').val();
        var profile_id = network.attr('data-profile');
        // Call ajax save status checked
        // set ajax data
        let data = {
            'action': 'profile_status_save',
            'is_check': is_check,
            'id': id,
            'profile_id': profile_id
        };
        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {

                if (response.success) {

                } else {
                    console.log('Error');
                }
            });

        const formatted_service = network.attr('data-formatted-service');
        let dataService = network.attr('data-service');
        const datalength = network.attr('data-urllength');
        const dataNetwork = network.attr('data-network');

        const ecpProfileData = contentShareBox.find('.ecp-profile-data');

        ecpProfileData.attr('data-avatar', network.attr('data-avatar'));
        ecpProfileData.attr('data-network', dataNetwork);

        let servicePrefix = 'facebook-group';
        if (dataService === '')
            dataService = servicePrefix;

        const postTextInfo = document.querySelector('.ecp-posttext-texts-share .ecp-posttext.' + dataService);

        const ecpServicePostText = contentShareBox.find('.ecp-service-post-text');

        var is_image = contentShareBox.find('.is_image').val();
        if (is_image == 1) {
            var lengthSet = postTextInfo.dataset.limit - parseInt(datalength) - parseInt(ecpServicePostText.val().length);
        } else {
            var lengthSet = postTextInfo.dataset.limit - parseInt(ecpServicePostText.val().length);
        }

        contentShareBox.find('.counterNumberShare').text(lengthSet);
        contentShareBox.find('.ecp-posttext-info').html(postTextInfo.innerHTML);
        contentShareBox.find('.ecp-posttext-info').find('.post-limit-share').html(lengthSet);
        contentShareBox.find('.ecp-post-image').attr('data-profile', network.attr('data-profile'));

        ecpServicePostText.attr('data-counter', postTextInfo.dataset.limit);
        ecpServicePostText.attr('data-length', lengthSet);

        ecpServicePostText.attr('data-service', dataService);
        ecpServicePostText.attr('data-network', dataNetwork);
        ecpServicePostText.attr('data-urllength', datalength);

        // if ('twitter-profile' != dataService)
        //     ecpServicePostText.attr('maxlength', postTextInfo.dataset.limit);

        ecpServicePostText.attr('data-profile', network.attr('data-profile'));

        // needed to re-bind the event as .on() is not working for cloned nodes
        ecpServicePostText.bind('keyup', updateCounterShare);

        contentShareBox.find('.url-length').val(datalength);
        contentShareBox.find('.ecp-service').val(dataService);

        if (is_immediately != 1) {
            // contentShareBox.find('.ecp-save-post-share').on('click', saveNetworkPostShare);
        }

        contentShareBox.find('.ecp-social-name-add-post').val(formatted_service);

        contentShareBox.find('.ecp-save-post-share').attr('data-network', dataNetwork);
        contentShareBox.find('.ecp-save-post-share').attr('data-status-text', '1');
        if (dataNetwork == 'instagram' || dataNetwork == 'pinterest') {
            contentShareBox.find('.ecp-save-post-share').attr('data-status-image', '0');
            contentShareBox.find('.ecp-save-post-share').attr('disabled', true);
        } else {
            contentShareBox.find('.ecp-save-post-share').attr('data-status-image', '1');
        }
    });

    $('.ecp-edit-message').on('click', function (e) {
        e.preventDefault();
        let _this = $(this);
        let contentShareBox = _this.parents('.ecp-content-share-network');
        contentShareBox.find('.ecp-content-box').removeClass('hidden');
        const network = $(this);

        const formatted_service = network.attr('data-formatted-service');
        let dataService = network.attr('data-service');
        const datalength = network.attr('data-urllength');
        const dataNetwork = network.attr('data-network');

        const ecpProfileData = contentShareBox.find('.ecp-profile-data');

        ecpProfileData.attr('data-avatar', network.attr('data-avatar'));
        ecpProfileData.attr('data-network', dataNetwork);

        let servicePrefix = 'facebook-group';
        if (dataService === '')
            dataService = servicePrefix;

        // Get current post text
        var current_text = contentShareBox.find('.ecp-service-post-text').val();
        var current_lenght = current_text.length;
        const postTextInfo = document.querySelector('.ecp-posttext-texts-share .ecp-posttext.' + dataService);

        const ecpServicePostText = contentShareBox.find('.ecp-service-post-text');

        var is_image = contentShareBox.find('.is_image').val();
        if (is_image == 1) {
            var lengthSet = postTextInfo.dataset.limit - parseInt(datalength) - parseInt(ecpServicePostText.val().length);
        } else {
            var lengthSet = postTextInfo.dataset.limit - parseInt(ecpServicePostText.val().length);
        }

        contentShareBox.find('.counterNumberShare').text(lengthSet);
        contentShareBox.find('.ecp-posttext-info').html(postTextInfo.innerHTML);
        contentShareBox.find('.ecp-posttext-info').find('.post-limit-share').html(lengthSet);
        contentShareBox.find('.ecp-post-image').attr('data-profile', network.attr('data-profile'));

        ecpServicePostText.attr('data-counter', postTextInfo.dataset.limit);
        ecpServicePostText.attr('data-length', lengthSet);

        ecpServicePostText.attr('data-service', dataService);
        ecpServicePostText.attr('data-network', dataNetwork);
        ecpServicePostText.attr('data-urllength', datalength);

        // if ('twitter-profile' != dataService)
        //     ecpServicePostText.attr('maxlength', postTextInfo.dataset.limit);

        ecpServicePostText.attr('data-profile', network.attr('data-profile'));

        // needed to re-bind the event as .on() is not working for cloned nodes
        ecpServicePostText.bind('keyup', updateCounterShare);

        contentShareBox.find('.url-length').val(datalength);
        contentShareBox.find('.ecp-service').val(dataService);

        // contentShareBox.find('.ecp-save-post-share').on('click', saveNetworkPostShare);

        contentShareBox.find('.ecp-social-name-add-post').val(formatted_service);

        contentShareBox.find('.ecp-save-post-share').attr('data-network', dataNetwork);
        contentShareBox.find('.ecp-save-post-share').attr('data-status-text', '1');
        contentShareBox.find('.ecp-save-post-share').attr('data-status-image', '1');
    });

    $(document).on('click', '.ecp-save-post-share', saveNetworkPostShare);

    $(document).on('keyup', '.ecp-service-post-text-library', function () {
        var post_id = $(this).attr('data-id');
        const content = this.value;

        const limit = parseInt(this.dataset.counter);
        var url_length = $(this).parents('.ecp-post-saved-template').find('.url-length').val();
        var is_image = $(this).parents('.ecp-post-saved-template').find('.is_image').val();
        var network = $(this).attr('data-network');
        if (is_image == 1) {
            if (network != 'twitter') {
                var count = limit - parseInt(this.value.length) - parseInt(url_length);
            } else {
                var countUrl = urlify(this.value);
                var content_length_new = replaceUrl(this.value);

                var count = limit - content_length_new - countUrl * 23;
            }
        } else {
            if (network != 'twitter') {
                var count = limit - parseInt(this.value.length);
            } else {
                var countUrl = urlify(this.value);
                var content_length_new = replaceUrl(this.value);

                var count = limit - content_length_new - countUrl * 23;
            }
        }
        // const links = getUrls(content);

        // if network is twitter, there are special rules to how character limit is implemented.
        // links no matter how long will always counts as 23 chars maximum
        // if ('twitter-profile' == this.dataset.network && links != undefined && links.length > 0) {

        //     // first get content length without URLs in the text
        //     let contentCopy = content;
        //     for (let i of links) {
        //         contentCopy = contentCopy.replace(i, '');
        //     }

        //     // second get lengths of each link, then add them
        //     let urlLengths = links.map( link => link.length > 23 ? 23 : link.length);
        //     let linkLengths = urlLengths.reduce( (a, b) => a + b);
        //     linkLengths += 1; // + the space before the link

        //     // deduct URL-less content length + total URL lengths from character limit for Twitter (280 max chars only)
        //     count = limit - (parseInt(contentCopy.length) + parseInt(linkLengths));
        // }

        //  save current content length to textarea attrib
        this.dataset.length = parseInt(count);

        // update the text count being shown
        this.previousElementSibling.querySelector('.counterNumber').innerText = count;
        // this.closest('.ecp-new-data-row ').querySelector('.ecp-posttext-info .post-limit').innerHTML = count;

        if (count > 0 && count <= limit) {
            this.classList.remove('ecp-input-error');
            $(this).parents('.ecp-input').find('.ecp-countdown').removeAttr('style');
            // $('#ecp_custom_message').removeAttr('style');
            $('#add-row-' + post_id).attr('disabled', false)
        } else if (count < 0) {
            $(this).parents('.ecp-input').find('.ecp-countdown').css('background-color', '#fb5654');
            // $('#ecp_custom_message').css({'border': '2px solid #fb5654', 'box-shadow':'none'});
            $('#add-row-' + post_id).attr('disabled', true)
        } else {
            $(this).parents('.ecp-input').find('.ecp-countdown').removeAttr('style');
            // $('#ecp_custom_message').removeAttr('style');
            $('#add-row-' + post_id).attr('disabled', false)
        }
    });

    $(document).on('click', '#ecp-profile-posts-block .ecp-image-upload', function (e) {
        e.preventDefault();

        var _this = $(this);

        var network = $(this).parents('#ecp-profile-posts-block').find('.ecp-service-post-text').attr('data-network');
        var current_status_text = $('.ecp-add-post').attr('data-status-text');
        var current_status_image = $('.ecp-add-post').attr('data-status-image');
        var is_image = _this.parents('#ecp-profile-posts-block').find('.is_image').val();
        var $ecp_profile_type = 'image';
        if( 'tiktok' == network ) {
            $ecp_profile_type = 'video';
        }
        var button = _this,
            ecp_uploader = wp.media({
                title: 'Custom image',
                library: {
                    uploadedTo: wp.media,
                    type: $ecp_profile_type
                },
                button: {
                    text: 'Use this image'
                },
                multiple: false
            }).on('select', function () {

                var attachment = ecp_uploader.state().get('selection').first().toJSON();

                $('.ecp-post-image').val(attachment.url);

                let imgDisplay = _this.parents('.ecp-posts-form-bottom-block').find('.ecp-post-image-display');

                if('tiktok' == network) {
                    _this.parents('#ecp-profile-posts-block').find('video.ecp-post-image-display.ecp-post-video-display').addClass('ecp-post-video-active');
                    _this.parents('#ecp-profile-posts-block').find('img.ecp-post-image-display').hide();
                }

                // const imgDisplay = button.parents('.ecp-posts-form-bottom-block').find('.ecp-post-image');


                if (current_status_image == '0') {
                    $('.ecp-add-post').attr('data-status-image', '1');
                    if (current_status_text == '1') {
                        $('.ecp-add-post').attr('disabled', false);
                    }
                }
                imgDisplay.attr("src", attachment.url);
                $('.upload-image-new').removeClass('hidden');

                $('.ecp-cancel-without-popup').addClass('ecp-hidden');
                $('.ecp-cancel-with-popup').removeClass('ecp-hidden');

                $('.upload-image-old').addClass('hidden');

                if (is_image == 0) {
                    // check counter number handle
                    var current_length = _this.parents('#ecp-profile-posts-block').find('.ecp-service-post-text').attr('data-length');
                    var url_length = $('#url-length').val();

                    if (network != 'twitter') {
                        var length = parseInt(current_length) - parseInt(url_length);
                    } else {
                        var length = parseInt(current_length);
                    }

                    _this.parents('#ecp-profile-posts-block').find('.ecp-service-post-text').attr('data-length', length);
                    _this.parents('#ecp-profile-posts-block').find('.counterNumber').text(length);
                    _this.parents('#ecp-profile-posts-block').find('.post-limit').text(length);
                    _this.parents('#ecp-profile-posts-block').find('.is_image').val(1);

                    if (length < 0) {
                        _this.parents('#ecp-profile-posts-block').find('.post-limit').css({
                            'color': '#fb5654',
                            'font-weight': 'bold'
                        });
                        _this.parents('#ecp-profile-posts-block').find('.ecp-countdown').css('background-color', '#fb5654');
                        // $('#ecp_custom_message').css({'border': '2px solid #fb5654', 'box-shadow':'none'});
                        _this.parents('#ecp-profile-posts-block').find('.ecp-add-post').attr('data-status-text', '0');
                        _this.parents('#ecp-profile-posts-block').find('.ecp-add-post').attr('disabled', true);
                    }
                }
            })
                .open();
    });

    $(document).on('click', '.ecp-onboard-finish', function (e) {
        e.preventDefault();
        var href = $(this).attr('href');

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'finishOnboard'
            },
            success: function (response) {
                if (response.success) {
                    window.location.href = href;
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    });

    $(document).on('click', '.ecp-content-share-network .ecp-image-upload-share', function (e) {
        e.preventDefault();

        var _this = $(this);

        var network = $(this).parents('.ecp-content-share-network').find('.ecp-service-post-text').attr('data-network');
        var current_status_text = $(this).parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-text');
        var current_status_image = $(this).parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-image');
        var is_image = _this.parents('.ecp-content-share-network').find('.is_image').val();
        var $ecp_profile_type = 'image';
        if( 'tiktok' == network ) {
            $ecp_profile_type = 'video';
        }
        var button = _this,
            ecp_uploader = wp.media({
                title: 'Custom image',
                library: {
                    uploadedTo: wp.media,
                    type: $ecp_profile_type
                },
                button: {
                    text: 'Use this image'
                },
                multiple: false
            }).on('select', function () {

                var attachment = ecp_uploader.state().get('selection').first().toJSON();

                _this.parents('.ecp-content-share-network').find('.ecp-post-image-share').val(attachment.url);

                let imgDisplay = _this.parents('.ecp-content-share-network').find('.ecp-post-image-display');


                if (current_status_image == '0') {
                    _this.parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-image', '1');
                    if (current_status_text == '1') {
                        _this.parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('disabled', false);
                    }
                }

                imgDisplay.attr("src", attachment.url);
                _this.parents('.ecp-content-share-network').find('.upload-image-new-share').removeClass('hidden');

                // $('.ecp-cancel-without-popup').addClass('ecp-hidden');
                // $('.ecp-cancel-with-popup').removeClass('ecp-hidden');

                _this.parents('.ecp-content-share-network').find('.upload-image-old-share').addClass('hidden');

                if (is_image == 0) {
                    // check counter number handle
                    var current_length = _this.parents('.ecp-content-share-network').find('.ecp-service-post-text').attr('data-length');

                    var url_length = _this.parents('.ecp-content-share-network').find('.url-length').val();

                    if (network != 'twitter') {
                        var length = parseInt(current_length) - parseInt(url_length);
                    } else {
                        var length = parseInt(current_length);
                    }

                    _this.parents('.ecp-content-share-network').find('.ecp-service-post-text').attr('data-length', length);
                    _this.parents('.ecp-content-share-network').find('.counterNumberShare').text(length);
                    _this.parents('.ecp-content-share-network').find('.post-limit-share').text(length);
                    _this.parents('.ecp-content-share-network').find('.is_image').val(1);

                    if (length < 0) {
                        _this.parents('.ecp-content-share-network').find('.post-limit-share').css({
                            'color': '#fb5654',
                            'font-weight': 'bold'
                        });
                        _this.parents('.ecp-content-share-network').find('.ecp-countdown').css('background-color', '#fb5654');
                        _this.parents('.ecp-content-share-network').find('.ecp-add-post').attr('data-status-text', '0');
                        _this.parents('.ecp-content-share-network').find('.ecp-add-post').attr('disabled', true);
                    }
                }
            })
                .open();
    });

    $(document).on('click', '#ecp-profile-posts-block .ecp-fileinput', function (e) {
        e.preventDefault();

        var _this = $(this);
        var network = $(this).parents('#ecp-profile-posts-block').find('.ecp-service-post-text').attr('data-network');
        var current_status_text = $('.ecp-add-post').attr('data-status-text');
        var current_status_image = $('.ecp-add-post').attr('data-status-image');
        var is_image = _this.parents('#ecp-profile-posts-block').find('.is_image').val();
        var $ecp_profile_type = 'image';
        if( 'tiktok' == network ) {
            $ecp_profile_type = 'video';
        }
        var button = $(this),
            ecp_uploader = wp.media({
                title: 'Custom image',
                library: {
                    uploadedTo: wp.media,
                    type: $ecp_profile_type
                },
                button: {
                    text: 'Use this image'
                },
                multiple: false
            }).on('select', function () {

                var attachment = ecp_uploader.state().get('selection').first().toJSON();

                $('.ecp-post-image').val(attachment.url);

                const imgDisplay = button.parents('.ecp-posts-form-bottom-block').find('.ecp-post-image-display');
                // const imgDisplay = button.parents('.ecp-posts-form-bottom-block').find('.ecp-post-image');


                if (current_status_image == '0') {
                    $('.ecp-add-post').attr('data-status-image', '1');
                    if (current_status_text == '1') {
                        $('.ecp-add-post').attr('disabled', false);
                    }
                }
                imgDisplay.attr("src", attachment.url);
                $('.upload-image-new').removeClass('hidden');

                $('.ecp-cancel-without-popup').addClass('ecp-hidden');
                $('.ecp-cancel-with-popup').removeClass('ecp-hidden');

                $('.upload-image-old').addClass('hidden');

                if (is_image == 0) {
                    // check counter number handle
                    var current_length = _this.parents('#ecp-profile-posts-block').find('.ecp-service-post-text').attr('data-length');
                    var url_length = $('#url-length').val();

                    if (network != 'twitter') {
                        var length = parseInt(current_length) - parseInt(url_length);
                    } else {
                        var length = parseInt(current_length);
                    }

                    _this.parents('#ecp-profile-posts-block').find('.ecp-service-post-text').attr('data-length', length);
                    _this.parents('#ecp-profile-posts-block').find('.counterNumber').text(length);
                    _this.parents('#ecp-profile-posts-block').find('.post-limit').text(length);
                    _this.parents('#ecp-profile-posts-block').find('.is_image').val(1);

                    if (length < 0) {
                        _this.parents('#ecp-profile-posts-block').find('.post-limit').css({
                            'color': '#fb5654',
                            'font-weight': 'bold'
                        });
                        _this.parents('#ecp-profile-posts-block').find('.ecp-countdown').css('background-color', '#fb5654');
                        // $('#ecp_custom_message').css({'border': '2px solid #fb5654', 'box-shadow':'none'});
                        _this.parents('#ecp-profile-posts-block').find('.ecp-add-post').attr('data-status-text', '0');
                        _this.parents('#ecp-profile-posts-block').find('.ecp-add-post').attr('disabled', true);
                    }
                }

                _this.parents('.ecp-image-actions').find('.ecp-image-delete-add-media-saved').removeClass('hidden');
            })
                .open();

    });

    $(document).on('click', '.ecp-content-share-network .ecp-fileinput', function (e) {
        e.preventDefault();

        var _this = $(this);
        var network = $(this).parents('.ecp-content-share-network').find('.ecp-service-post-text').attr('data-network');
        var current_status_text = $(this).parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-text');
        var current_status_image = $(this).parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-image');
        var is_image = _this.parents('.ecp-content-share-network').find('.is_image').val();
        var $ecp_profile_type = 'image';
        if( 'tiktok' == network ) {
            $ecp_profile_type = 'video';
        }
        var button = $(this),
            ecp_uploader = wp.media({
                title: 'Custom image',
                library: {
                    uploadedTo: wp.media,
                    type: $ecp_profile_type
                },
                button: {
                    text: 'Use this image'
                },
                multiple: false
            }).on('select', function () {

                var attachment = ecp_uploader.state().get('selection').first().toJSON();

                _this.parents('.ecp-content-share-network').find('.ecp-post-image-share').val(attachment.url);

                const imgDisplay = _this.parents('.ecp-content-share-network').find('.ecp-post-image-display');

                if (current_status_image == '0') {
                    button.parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-image', '1');
                    if (current_status_text == '1') {
                        button.parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('disabled', false);
                    }
                }
                imgDisplay.attr("src", attachment.url);
                button.parents('.ecp-content-share-network').find('.upload-image-new-share').removeClass('hidden');

                // $('.ecp-cancel-without-popup').addClass('ecp-hidden');
                // $('.ecp-cancel-with-popup').removeClass('ecp-hidden');

                button.parents('.ecp-content-share-network').find('.upload-image-old-share').addClass('hidden');

                if (is_image == 0) {
                    // check counter number handle
                    var current_length = _this.parents('.ecp-content-share-network').find('.ecp-service-post-text').attr('data-length');
                    var url_length = _this.parents('.ecp-content-share-network').find('.url-length').val();

                    if (network != 'twitter') {
                        var length = parseInt(current_length) - parseInt(url_length);
                    } else {
                        var length = parseInt(current_length);
                    }

                    _this.parents('.ecp-content-share-network').find('.ecp-service-post-text').attr('data-length', length);
                    _this.parents('.ecp-content-share-network').find('.counterNumberShare').text(length);
                    _this.parents('.ecp-content-share-network').find('.post-limit-share').text(length);
                    _this.parents('.ecp-content-share-network').find('.is_image').val(1);

                    if (length < 0) {
                        _this.parents('.ecp-content-share-network').find('.post-limit-share').css({
                            'color': '#fb5654',
                            'font-weight': 'bold'
                        });
                        _this.parents('.ecp-content-share-network').find('.ecp-countdown').css('background-color', '#fb5654');
                        // $('#ecp_custom_message').css({'border': '2px solid #fb5654', 'box-shadow':'none'});
                        _this.parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-text', '0');
                        _this.parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('disabled', true);
                    }
                }

                _this.parents('.ecp-image-actions').find('.ecp-image-delete-add-media-saved').removeClass('hidden');
            })
                .open();

    });

    $(document).on('click', '#ecp-saved-posts-block .ecp-post-img-bg .ecp-fileinput', function (e) {
        e.preventDefault();

        var _this = $(this);

        var current_status_text = $('.ecp-add-post').attr('data-status-text');
        var current_status_image = $('.ecp-add-post').attr('data-status-image');
        var is_image = _this.parents('.ecp-image-actions').find('.is_image').val();
        var network = _this.parents('.ecp-image-actions').find('.ecp-network').val();
        var $ecp_profile_type = 'image';
        if( 'tiktok' == network ) {
            $ecp_profile_type = 'video';
        }
        var post_id = $(this).attr('data-id');
        var button = $(this),
            ecp_uploader = wp.media({
                title: 'Custom image',
                library: {
                    uploadedTo: wp.media,
                    type: $ecp_profile_type
                },
                button: {
                    text: 'Use this image'
                },
                multiple: false
            }).on('select', function () {

                var attachment = ecp_uploader.state().get('selection').first().toJSON();

                $('.ecp-post-image').val(attachment.url);
                $('.ecp-upload-custom').find('.ecp-post-image').each(function (e) {
                    $(this).val("");
                });

                const imgDisplay = button.parents('.ecp-post-img-bg').find('.ecp-post-image-display');
                // const imgDisplay = button.parents('.ecp-posts-form-bottom-block').find('.ecp-post-image');

                $.ajax({
                    url: ajaxurl,
                    type: "post",
                    data: {
                        'action': 'ajaxChangePostImage',
                        'post_id': post_id,
                        'url': attachment.url
                    },
                    success: function (response) {
                        if (current_status_image == '0') {
                            $('.ecp-add-post').attr('data-status-image', '1');
                            if (current_status_text == '1') {
                                $('.ecp-add-post').attr('disabled', false);
                            }
                        }
                        imgDisplay.attr("src", attachment.url);

                        if (is_image == 0) {
                            // check counter number handle
                            var current_length = _this.parents('.ecp-post-saved-template').find('.ecp-service-post-text-library').attr('data-length');
                            var url_length = _this.parents('.ecp-post-saved-template').find('.url-length').val();

                            if (network != 'twitter') {
                                var length = parseInt(current_length) - parseInt(url_length);
                            } else {
                                var length = parseInt(current_length);
                            }

                            _this.parents('.ecp-post-saved-template').find('.ecp-service-post-text-library').attr('data-length', length);
                            _this.parents('.ecp-post-saved-template').find('.counterNumber').text(length);

                            if (length < 0) {
                                _this.parents('.ecp-post-saved-template').find('.ecp-countdown').css('background-color', '#fb5654');
                                // $('#ecp_custom_message').css({'border': '2px solid #fb5654', 'box-shadow':'none'});
                                _this.parents('.ecp-post-saved-template').find('.change-post-in-library').attr('disabled', true);
                                _this.parents('.ecp-post-saved-template').find('.content-edit').removeClass('hidden');
                                _this.parents('.ecp-post-saved-template').find('.content-show').addClass('hidden');
                            }

                            _this.parents('.ecp-image-actions').find('.is_image').val(1);
                        }

                        _this.parents('.ecp-image-actions').find('.ecp-image-delete-add-media-saved').removeClass('hidden');

                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert(errorThrown);
                    }
                });

            })
                .open();
    });

    $(document).on('click', '.dashicons-edit-page', function (e) {
        e.preventDefault();
        $(this).parents('.ecp-post-saved-template').find('.content-show').addClass('hidden');
        $(this).parents('.ecp-post-saved-template').find('.content-edit').removeClass('hidden');
    });

    $(document).on('click', '.cancel-change', function (e) {
        e.preventDefault();
        $(this).parents('.ecp-post-saved-template').find('.content-show').removeClass('hidden');
        $(this).parents('.ecp-post-saved-template').find('.content-edit').addClass('hidden');
    });
    $(document).on('click', '.ecp-image-delete-add-media', function (e) {
        var _this = $(this);
        var network = $(this).parents('.ecp-post-fields-container').find('.ecp-add-post').attr('data-network');
        e.preventDefault();
        var defaultImage = $('.url-plugin').val();
        defaultImage = defaultImage + '/admin/img/ecp-no-visual-attached.png';
        $(this).parents('.ecp-image-actions').find('.ecp-post-image').val('');
        $(this).parents('.ecp-image-actions').find('.ecp-post-image').attr('src', '');
        $(this).parents('.ecp-upload-custom').find('.ecp-post-image-display').attr('src', defaultImage);
        $('.upload-image-new').addClass('hidden');
        $('.upload-image-old').removeClass('hidden');
        var content = $(this).parents('.ecp-post-fields-container').find('#ecp_custom_message').val();
        if (content.length == 0) {
            $('.ecp-cancel-without-popup').removeClass('ecp-hidden');
            $('.ecp-cancel-with-popup').addClass('ecp-hidden');
        }

        if (network == 'instagram' || network == "pinterest") {
            $('.ecp-add-post').attr('data-status-image', '0');
            $('.ecp-add-post').attr('disabled', true);
        }

        var current_length = _this.parents('#ecp-profile-posts-block').find('.ecp-service-post-text').attr('data-length');
        var url_length = $('#url-length').val();

        if (network != 'twitter') {
            var length = parseInt(current_length) + parseInt(url_length);
        } else {
            var length = parseInt(current_length);
        }

        _this.parents('#ecp-profile-posts-block').find('.ecp-service-post-text').attr('data-length', length);
        _this.parents('#ecp-profile-posts-block').find('.counterNumber').text(length);
        _this.parents('#ecp-profile-posts-block').find('.post-limit').text(length);
        _this.parents('#ecp-profile-posts-block').find('.is_image').val(0);


        var current_status_text = $('.ecp-add-post').attr('data-status-text');
        var current_status_image = $('.ecp-add-post').attr('data-status-image');
        if (length >= 0) {
            _this.parents('#ecp-profile-posts-block').find('.post-limit').removeAttr('style');
            _this.parents('#ecp-profile-posts-block').find('.ecp-countdown').removeAttr('style');
            if (current_status_text == '0') {
                $('.ecp-add-post').attr('data-status-text', '1');
                if (current_status_image == 1) {
                    $('.ecp-add-post').attr('disabled', false);
                }
            }
        }
    });

    $(document).on('click', '.ecp-image-delete-add-media-share', function (e) {
        var _this = $(this);
        var network = $(this).parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-network');
        e.preventDefault();
        var defaultImage = $('.url-plugin').val();
        defaultImage = defaultImage + '/admin/img/ecp-no-visual-attached.png';
        $(this).parents('.ecp-image-actions').find('.ecp-post-image-share').val('');
        $(this).parents('.ecp-image-actions').find('.ecp-post-image-share').attr('src', '');
        $(this).parents('.ecp-upload-custom').find('.ecp-post-image-display').attr('src', defaultImage);
        $(this).parents('.ecp-content-share-network').find('.upload-image-new-share').addClass('hidden');
        $(this).parents('.ecp-content-share-network').find('.upload-image-old-share').removeClass('hidden');
        var content = $(this).parents('.ecp-content-share-network').find('.ecp-service-post-text').val();
        if (content.length == 0) {
            // $('.ecp-cancel-without-popup').removeClass('ecp-hidden');
            // $('.ecp-cancel-with-popup').addClass('ecp-hidden');
        }

        if (network == 'instagram' || network == "pinterest") {
            $(this).parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-image', '0');
            $(this).parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('disabled', true);
        }

        var current_length = _this.parents('.ecp-content-share-network').find('.ecp-service-post-text').attr('data-length');
        var url_length = _this.parents('.ecp-content-share-network').find('.url-length').val();

        if (network != 'twitter') {
            var length = parseInt(current_length) + parseInt(url_length);
        } else {
            var length = parseInt(current_length);
        }

        _this.parents('.ecp-content-share-network').find('.ecp-service-post-text').attr('data-length', length);
        _this.parents('.ecp-content-share-network').find('.counterNumberShare').text(length);
        _this.parents('.ecp-content-share-network').find('.post-limit-share').text(length);
        _this.parents('.ecp-content-share-network').find('.is_image').val(0);


        var current_status_text = _this.parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-text');
        var current_status_image = _this.parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-image');
        if (length >= 0) {
            _this.parents('.ecp-content-share-network').find('.post-limit-share').removeAttr('style');
            _this.parents('.ecp-content-share-network').find('.ecp-countdown').removeAttr('style');
            if (current_status_text == '0') {
                _this.parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('data-status-text', '1');
                if (current_status_image == 1) {
                    _this.parents('.ecp-content-share-network').find('.ecp-save-post-share').attr('disabled', false);
                }
            }
        }
    });

    $(document).on('click', '.ecp-image-delete-add-media-saved', function (e) {
        e.preventDefault();
        var _this = $(this);
        _this.parents('.ecp-post-saved-template').addClass('loading');
        var defaultImage = $('.url-plugin').val();
        defaultImage = defaultImage + '/admin/img/ecp-no-visual-attached.png';
        var post_id = $(this).parents('.ecp-post-saved-template').find('.ecp-post-id').val();

        var is_image = _this.parents('.ecp-image-actions').find('.is_image').val();
        var network = _this.parents('.ecp-image-actions').find('.ecp-network').val();

        // call ajax
        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxDeleteMedia',
                'post_id': post_id,
            },
            success: function (response) {
                _this.parents('.ecp-post-img-bg').find('.ecp-post-image-display').attr('src', defaultImage);
                _this.parents('.ecp-post-saved-template').removeClass('loading');
                _this.addClass('hidden');

                if (is_image == 1) {
                    var current_length = _this.parents('.ecp-post-saved-template').find('.ecp-service-post-text-library').attr('data-length');
                    var url_length = _this.parents('.ecp-post-saved-template').find('.url-length').val();

                    if (network != 'twitter') {
                        var length = parseInt(current_length) + parseInt(url_length);
                    } else {
                        var length = parseInt(current_length);
                    }

                    _this.parents('.ecp-post-saved-template').find('.ecp-service-post-text-library').attr('data-length', length);
                    _this.parents('.ecp-post-saved-template').find('.counterNumber').text(length);

                    if (length >= 0) {
                        _this.parents('.ecp-post-saved-template').find('.ecp-countdown').removeAttr('style');
                        _this.parents('.ecp-post-saved-template').find('.change-post-in-library').attr('disabled', false);
                    }

                    _this.parents('.ecp-image-actions').find('.is_image').val(0);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                _this.parents('.ecp-post-saved-template').removeClass('loading');
                alert(errorThrown);
            }
        });

    });

    $(document).on('click', '.change-post-in-library', function (e) {
        e.preventDefault();
        var _this = $(this);
        _this.parents('.ecp-post-saved-template').addClass('loading');
        var post_id = $(this).attr('data-id');
        var ecp_change_post_in_library_nonce = $(this).attr('data-ecp-change-post-in-library-nonce');
        var content = $('.text-content-' + post_id).val();
        var category_id = _this.parents('.content-edit').find('.ecp-category-library').val();
        var category_name = _this.parents('.content-edit').find('.ecp-category-library option:selected').text();

        var old_category_id = _this.parents('.ecp-post-saved-template').find('.saved-category-id').val();

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxChangePostContent',
                'post_id': post_id,
                'content': content,
                'category_id': category_id,
                'ecp_change_post_in_library_nonce': ecp_change_post_in_library_nonce

            },
            success: function (response) {
                if (response.success === true) {
                    // var wp_post_link = _this.parents('.ecp-post-saved-template').find('.content-show .ecp-library-wp-post-link').attr('href');
                    _this.parents('.ecp-post-saved-template').find('.ecp-post-content-show').html(urlify2(content));
                    _this.parents('.ecp-post-saved-template').find('.content-show').removeClass('hidden');
                    _this.parents('.ecp-post-saved-template').find('.content-show').find('.category-name').html('');
                    _this.parents('.ecp-post-saved-template').find('.content-show').find('.category-name').html(category_name);
                    _this.parents('.ecp-post-saved-template').find('.content-edit').addClass('hidden');
                    _this.parents('.ecp-post-saved-template').find('.content-edit').find('.category-name').html('');
                    _this.parents('.ecp-post-saved-template').find('.content-edit').find('.category-name').html(category_name);
                    _this.parents('.ecp-post-saved-template').find('.saved-category-id').val(category_id);

                    if (category_id != old_category_id) {
                        var temp_old = $('#category_box_' + old_category_id).find('.posts_number_all').text();
                        temp_old = parseInt(temp_old) - 1;

                        var temp = $('#category_box_' + category_id).find('.posts_number_all').text();
                        temp = parseInt(temp) + 1;

                        $('#category_box_' + old_category_id).find('.posts_number_all').text(temp_old);

                        $('#category_box_' + category_id).find('.posts_number_all').text(temp);
                    }
                } else {
                    alert(response.data);
                }
                _this.parents('.ecp-post-saved-template').removeClass('loading');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                _this.parents('.ecp-post-saved-template').removeClass('loading');
                alert(errorThrown);
            }
        });
    });

    $(document).on('click', '#ecp-saved-posts-block .ecp-post-delete', deleteSavedPost);
    $(document).on('click', '.ecp-post-saved-template .ecp-post-delete', deleteSavedPost);

    // removed temporary changes
    $('#save_settings').on('click', function () {

        // set ajax data
        const data = {
            'action': 'save_settings',
            'fields': $('#ecp_settings_form').serialize(),
            'nonce': document.querySelector('#evergreen-settings-save').value
        };

        const saveLoader = document.querySelector('.ecp-loading-saving');

        saveLoader.classList.add('show')

        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {

                if (response.success) {
                    setTimeout(function () {
                        saveLoader.classList.add('ecp-loading-success');
                        saveLoader.innerText = document.querySelector('label.saving-success').innerText;
                        toggleSaveSetting(false);
                    }, 500);

                    // update all field(s) saved defaults
                    updateSavedFields();

                } else {
                    saveLoader.innerText = document.querySelector('label.saving-error').innerText;
                }

                setTimeout(function () {
                    saveLoader.classList.remove('ecp-loading-success');
                    saveLoader.classList.remove('show');
                    saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                }, 2000);
            });
    });

    // removed temporary changes
    $('#save_settings_config').on('click', function (e) {
        e.preventDefault();
        $('.ecp-show-result').addClass('hidden');

        // set ajax data
        const data = {
            'action': 'save_settings_config',
            'fields': $('#ecp_settings_config_form').serialize(),
            'nonce': document.querySelector('#evergreen-settings-config-save').value
        };

        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {
                if (response.success) {
                    $('.ecp-show-result').text(document.querySelector('label.saving-success').innerText);
                    toggleSaveSetting(false);
                    // update all field(s) saved defaults
                    updateSavedFields();
                    $('.ecp-show-result').css('color', '#71c069');

                } else {
                    $('.ecp-show-result').text(document.querySelector('label.saving-error').innerText);
                    $('.ecp-show-result').css('color', '#fb5654');
                }

                $('.ecp-show-result').removeClass('hidden');
            });
    });

    $('#save_settings_config_tab1').on('click', function () {

        $('.ecp-show-result').addClass('hidden');

        // set ajax data
        const data = {
            'action': 'save_settings_config',
            'fields': $('#ecp_settings_config_form').serialize(),
            'nonce': document.querySelector('#evergreen-settings-config-save').value
        };

        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {
                if (response.success) {
                    $('.ecp-show-result').text(document.querySelector('label.saving-success').innerText);
                    toggleSaveSetting(false);
                    // update all field(s) saved defaults
                    updateSavedFields();
                    $('.ecp-show-result').css('color', '#71c069');

                } else {
                    $('.ecp-show-result').text(document.querySelector('label.saving-error').innerText);
                    $('.ecp-show-result').css('color', '#fb5654');
                }

                $('.ecp-show-result').removeClass('hidden');
            });
    });

    $('#save_settings_config_tab2').on('click', function () {

        $('.ecp-show-result').addClass('hidden');

        // set ajax data
        const data = {
            'action': 'save_settings_config',
            'fields': $('#ecp_settings_config_form').serialize(),
            'nonce': document.querySelector('#evergreen-settings-config-save').value
        };

        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {
                if (response.success) {
                    $('.ecp-show-result').text(document.querySelector('label.saving-success').innerText);
                    toggleSaveSetting(false);
                    // update all field(s) saved defaults
                    updateSavedFields();
                    $('.ecp-show-result').css('color', '#71c069');

                } else {
                    $('.ecp-show-result').text(document.querySelector('label.saving-error').innerText);
                    $('.ecp-show-result').css('color', '#fb5654');
                }

                $('.ecp-show-result').removeClass('hidden');
            });
    });

    $('.field-setting').on('change', function () {
        const field = $(this);
        const savedField = field.data('ref');

        field.removeClass('field-updated');
        if (field.val() != $('#' + savedField).val()) {
            field.addClass('field-updated');
        }

        toggleSaveSetting(false);
        if ($('.field-updated').length > 0) {
            toggleSaveSetting(true);
            field.removeClass('field-updated');
        }
    });


    $(document).on('change', '.ecp_category_sort', function (e) {
        var category_id = $(this).val();
        var order = $('.ecp_library_sort_order').val();
        var query = encodeURIComponent($('.ecp_content_search').val());
        var ecp_ajax_post_search_nonce = $(this).attr('data-ecp-ajax-post-search-nonce');
        // call ajax to handle show list post
        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxPostsSearch',
                'category_id': category_id,
                'order': order,
                'query': query,
                'ecp_ajax_post_search_nonce': ecp_ajax_post_search_nonce
            },
            success: function (response) {
                $('#ecp-post-list').html(response.data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    });

    $(document).on('change', '.ecp_library_sort_order', function (e) {
        var order = encodeURIComponent($(this).val());
        var query = encodeURIComponent($('.ecp_content_search').val());
        var category_id = $('.ecp_category_sort').val();
        var ecp_ajax_post_search_nonce = $(this).attr('data-ecp-ajax-post-search-nonce');
        // call ajax to handle show list post
        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxPostsSearch',
                'category_id': category_id,
                'order': order,
                'query': query,
                'ecp_ajax_post_search_nonce': ecp_ajax_post_search_nonce
            },
            success: function (response) {
                $('#ecp-post-list').html(response.data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    });

    $(document).on('keyup', '.ecp_content_search', delay(function (e) {
        var order = encodeURIComponent($(this).val());
        var query = encodeURIComponent($('.ecp_content_search').val());
        var category_id = $('.ecp_category_sort').val();
        var ecp_ajax_post_search_nonce = $(this).attr('data-ecp-ajax-post-search-nonce');
        // call ajax to handle show list post
        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxPostsSearch',
                'category_id': category_id,
                'order': order,
                'query': query,
                'ecp_ajax_post_search_nonce': ecp_ajax_post_search_nonce
            },
            success: function (response) {
                $('#ecp-post-list').html(response.data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            }
        });
    }, 1000));

    $(document).on('keyup', '.ecp_category_name', function (e) {
        var category_name = $(this).val();

        if (category_name.length > 0) {
            $('.category_add_new').attr('disabled', false);
        } else {
            $('.category_add_new').attr('disabled', true);
        }
    });

    $(document).on('click', '.category_cancel', function (e) {
        $('.ecp_category_name').val('');
        $('#ecp_add_category').addClass('ecp-hidden');
    });

    $(document).on('click', '.ecp-show-categories', function (e) {
        e.preventDefault();

        $('#ecp_edit_category').removeClass('ecp-hidden');
        $('.ecp-show-edit').addClass('ecp-hidden');
        $('.ecp-hide-edit').removeClass('ecp-hidden');
    });

    $(document).on('click', '.ecp-hide-categories', function (e) {
        e.preventDefault();

        $('#ecp_edit_category').addClass('ecp-hidden');
        $('.ecp-hide-edit').addClass('ecp-hidden');
        $('.ecp-show-edit').removeClass('ecp-hidden');
    });

    $(document).on('click', '.category_add_new', function (e) {
        var category_name = $('.ecp_category_name').val();
        var random = $('.ecp-checkbox-toggle').attr('data-status');
        var ecp_category_add_new_nonce = $(this).attr('data-ecp-category-add-new-nonce');

        if (category_name.length > 0) {

            $('.saving-category-name').text(category_name);
            const saveLoader = document.querySelector('.ecp-loading-saving');

            saveLoader.innerText = document.querySelector('label.saving-category').innerText;
            saveLoader.classList.add('show');

            const savedCategory = $('.ecp-category-fields-base .category_box').clone();

            $.ajax({
                url: ajaxurl,
                type: "post",
                data: {
                    'action': 'ajaxAddCategory',
                    'random': random,
                    'category_name': category_name,
                    'ecp_category_add_new_nonce': ecp_category_add_new_nonce
                },
                success: function (response) {

                    if (response.data.error == 0) {
                        saveLoader.innerText = response.data.message;
                    } else {
                        $('.ecp_category_select').each(function () {
                            $(this).append('<option value="' + response.data.category_id + '">' + response.data.category_name + '</option>')
                        });


                        savedCategory.attr('id', 'category_box_' + response.data.category_id);
                        savedCategory.find('.category-id').val(response.data.category_id);
                        savedCategory.find('.ecp-checkbox-toggle-small-random').attr('data-random', random);
                        savedCategory.find('.ecp-category-name-show p').text(response.data.category_name);
                        savedCategory.find('.ecp-category-name-edit .category-name-input').val(response.data.category_name);
                        savedCategory.find('.ecp-category-name-edit .change-category-name').attr('data-id', response.data.category_id);

                        savedCategory.appendTo('.categories_list');
                        $('.ecp_category_name').val('');
                        $('#ecp_add_category').addClass('ecp-hidden');
                        saveLoader.classList.add('ecp-loading-success');
                        saveLoader.innerText = document.querySelector('label.saved-category').innerText;
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    saveLoader.innerText = document.querySelector('label.saving-error').innerText;
                }
            });
            setTimeout(function () {
                saveLoader.classList.remove('ecp-loading-success');
                saveLoader.classList.remove('show');
                saveLoader.innerText = document.querySelector('label.saving-default').innerText;
            }, 2000);
        }
    });

    $(document).on('click', '.add_new_cate_but', function () {
        $('#ecp_add_category').removeClass('ecp-hidden');
    });

    $(document).on('click', '.jquery-modal-close', function (e) {
        e.preventDefault();
        $.modal.close();
    });

});

function delay(callback, ms) {
    var timer = 0;
    return function () {
        var context = this, args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function () {
            callback.apply(context, args);
        }, ms || 0);
    };
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function setCookie(name, value, days) {
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "") + expires + "; path=/";
}

function eraseCookie(name) {
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

jQuery(document).ready(function ($) {
    var set_resolution = getCookie('ecp-set-resolution');

    if (typeof set_resolution === "undefined") {

        setCookie('ecp-width', screen.width);
        setCookie('ecp-height', screen.height);
    }

    setTimeout(function() {
        $('#buffervia-connected').fadeOut('slow');
    }, 500);

    setTimeout(function () {
        $('.ecp-loading-saving').removeClass('show');
        var saving_default = $('.saving-default').text();
        $('.ecp-loading-saving').text(saving_default);
    }, 5000);

    $(document).on('mouseenter', '.buffer-connected', function () {
        var button = $(this);
        button.addClass('ecp-red-button').removeClass('ecp-green-button');
        button.find('span.text.ecp-connect-to-buffer').text("Disconnect Buffer");
    });
    
    $(document).on('mouseleave', '.buffer-connected', function () {
        var button = $(this);
        button.addClass('ecp-green-button').removeClass('ecp-red-button');
        button.find('span.text.ecp-connect-to-buffer').text("Connected to Buffer");
    });

    $(document).on('mouseenter', '.twitter-connected', function () {
        var button = $(this);
        button.addClass('ecp-red-button').removeClass('ecp-green-button');
        button.find('span.text.ecp-connect-to-twitter').text("Disconnect Twitter");
    });
    
    $(document).on('mouseleave', '.twitter-connected', function () {
        var button = $(this);
        button.addClass('ecp-green-button').removeClass('ecp-red-button');
        button.find('span.text.ecp-connect-to-twitter').text("Connected to Twitter");
    });

    $(document).on('click', '.ecp-checkbox-toggle', function () {

        var toggler = $(this);
        toggler.parent().parent().siblings().toggleClass('ecp-google-active');
        var status = toggler.attr('data-status') == 'off' ? 'on' : 'off';
        toggler.attr('data-status', status);

        var checkboxName = toggler.data('field');
        var checkbox = document.querySelector('input[name="' + checkboxName + '"]');

        checkbox.checked = !checkbox.checked;

        // set save button to disabled by default
        toggleSaveSetting(false);

        const savedField = document.querySelector('input[name="saved_' + checkboxName + '"]');
        //
        if (savedField.value == 'N' && checkbox.checked == true) {
            toggleSaveSetting(true);
        }

        if (savedField.value == 'Y' && checkbox.checked == false) {
            toggleSaveSetting(true);
        }

    });

   // Google Business profile fields hide/show on dependent value.
   $(document).on('click', "input[type=radio]", function(ev) {
    var ecpSiblings = $(this).parent().parent();
        if (ev.currentTarget.value == "call-to-action") {
            ecpSiblings.siblings('.ecp-google-business-container-wrap.ecp-google-business-event').removeClass('ecp-google-active');
            ecpSiblings.siblings('.ecp-google-business-container-wrap.ecp-google-business-offer').removeClass('ecp-google-active');
            ecpSiblings.siblings('.ecp-google-business-container-wrap.ecp-google-business-whats-new').addClass('ecp-google-active');
        } else if (ev.currentTarget.value == "event") {
            ecpSiblings.siblings('.ecp-google-business-container-wrap.ecp-google-business-whats-new').removeClass('ecp-google-active');
            ecpSiblings.siblings('.ecp-google-business-container-wrap.ecp-google-business-offer').removeClass('ecp-google-active');
            ecpSiblings.siblings('.ecp-google-business-container-wrap.ecp-google-business-event').addClass('ecp-google-active');
        }
        else if (ev.currentTarget.value == "offer") {
            ecpSiblings.siblings('.ecp-google-business-container-wrap.ecp-google-business-whats-new').removeClass('ecp-google-active');
            ecpSiblings.siblings('.ecp-google-business-container-wrap.ecp-google-business-event').removeClass('ecp-google-active');
            ecpSiblings.siblings('.ecp-google-business-container-wrap.ecp-google-business-offer').addClass('ecp-google-active');
        }
    });

    // Validation on the keyup and click for the google business fields.
    $(document).on('keyup click', ".ecp-google-business-profile-post-text,.ecp-google-business-profile-post-date", function (e) {
        if ($(this).val().length > 0) {
           $(this).removeClass('required-input');
           $(this).removeClass('ecp-input-error');
        } else {
            $(this).addClass('required-input');
        }
    });

    // Google Business profile button link field hide/show on dependent value.
    $(document).on('change', ".ecp-google-business-profile-post-button-whats-new, .ecp-google-business-profile-post-button-event", function(){
        ecpCta = $(this).val();
        ecpEvent = $(this).val();
        var ecpSiblings = $(this).parent().parent().siblings();
        if ('No Button' == ecpCta || 'No Button' == ecpEvent) {
            ecpSiblings.removeClass('ecp-google-active');
        } else {
            ecpSiblings.addClass('ecp-google-active');
        }
    });

    //Added the datarangepicker for the google business event fields.
    $(document).on('focus',".ecp-google-business-event-date", function(e) {
        e.preventDefault();
        $("input[name='ecpEventDate']").daterangepicker();
    });

    //Added the datarangepicker for the google business offer fields.
    $(document).on('focus',".ecp-google-business-offer-date", function(e) {
        e.preventDefault();
        $("input[name='ecpOfferDate']").daterangepicker();
    });

    //Added the timepicker for the google business event fields.
    $(document).on('focus',".ecp-google-business-event-time", function(e) {
        e.preventDefault();
       $("input[name='ecpEventTime']").daterangepicker({
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 1,
        locale: {
            format: 'HH:mm:ss'
        }
        }).on('show.daterangepicker', function (ev, picker) {
            picker.container.find(".calendar-table").hide();
        });
    });

    //Added the timepicker for the google business offer fields.
    $(document).on('focus',".ecp-google-business-offer-time", function(e) {
        e.preventDefault();
       $("input[name='ecpOfferTime']").daterangepicker({
        timePicker: true,
        timePicker24Hour: true,
        timePickerIncrement: 1,
        locale: {
            format: 'HH:mm:ss'
        }
        }).on('show.daterangepicker', function (ev, picker) {
            picker.container.find(".calendar-table").hide();
        });
    });

    $(document).on('click', '#ecp_edit_category .ecp-checkbox-toggle-small', function () {

        var toggler = $(this);

        var status = toggler.attr('data-status') == 'off' ? 'on' : 'off';

        toggler.attr('data-status', status);

        var category_id = toggler.parents('.category_box').find('.category-id').val();
        toggler.find('.change_status').removeAttr('style');
        // Call ajax
        const saveLoader = document.querySelector('.ecp-loading-saving');

        saveLoader.classList.add('show')
        var ecp_change_category_status_nonce = $(this).attr('data-ecp-change-category-status-nonce');

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxChangeCategoryStatus',
                'category_id': category_id,
                'status': status,
                'ecp_change_category_status_nonce': ecp_change_category_status_nonce
            },
            success: function (response) {

                if (response.data.error == 0) {
                    saveLoader.innerText = response.data.message;
                } else {
                    $('.ecp_category_select').each(function () {
                        if (status == 'on') {
                            $(this).append('<option value="' + response.data.category_id + '">' + response.data.category_name + '</option>')
                        } else {
                            $("option[value=" + category_id + "]").remove();
                        }
                    });
                    saveLoader.classList.add('ecp-loading-success');
                    if (status == 'on') {
                        saveLoader.innerText = document.querySelector('label.saving-active').innerText;
                    } else {
                        saveLoader.innerText = document.querySelector('label.saving-inactive').innerText;
                    }

                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                saveLoader.innerText = document.querySelector('label.saving-error').innerText;
            }
        });
        setTimeout(function () {
            saveLoader.classList.remove('ecp-loading-success');
            saveLoader.classList.remove('show');
            saveLoader.innerText = document.querySelector('label.saving-default').innerText;
        }, 2000);

    });

    $(document).on('click', '.ecp-checkbox-toggle-small-random', function () {

        var toggler = $(this);

        var random = toggler.attr('data-random') == 'off' ? 'on' : 'off';

        toggler.attr('data-random', random);

        var category_id = toggler.parents('.category_box').find('.category-id').val();
        toggler.find('.change_random').removeAttr('style');
        // Call ajax
        const saveLoader = document.querySelector('.ecp-loading-saving');

        saveLoader.classList.add('show');

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxChangeCategoryRandom',
                'category_id': category_id,
                'random': random
            },
            success: function (response) {

                if (response.data.error == 0) {
                    saveLoader.innerText = response.data.message;
                } else {
                    $('.ecp_category_select').each(function () {
                        if (random == 'on') {
                            $(this).append('<option value="' + response.data.category_id + '">' + response.data.category_name + '</option>')
                        } else {
                            $("option[value=" + category_id + "]").remove();
                        }
                    });
                    saveLoader.classList.add('ecp-loading-success');
                    if (random == 'on') {
                        saveLoader.innerText = document.querySelector('label.saving-random-active').innerText;
                    } else {
                        saveLoader.innerText = document.querySelector('label.saving-random-active').innerText;
                    }

                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                saveLoader.innerText = document.querySelector('label.saving-error').innerText;
            }
        });
        setTimeout(function () {
            saveLoader.classList.remove('ecp-loading-success');
            saveLoader.classList.remove('show');
            saveLoader.innerText = document.querySelector('label.saving-default').innerText;
        }, 2000);

    });

    $(document).on('click', '.change-category-name', function (e) {
        var _this = $(this);
        var category_id = $(this).attr('data-id');
        var category_ecp_change_category_name_nonce = $(this).attr('data-ecp-change-category-name-nonce');
        var category_name = $(this).parents('.ecp-category-name-edit').find('.category-name-input').val();

        var category_name_old = $(this).parents('.category_box').find('.ecp-category-name-show p').text();
        // Call ajax
        const saveLoader = document.querySelector('.ecp-loading-saving');

        saveLoader.classList.add('show')

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxChangeCategoryName',
                'category_id': category_id,
                'category_name': category_name,
                'category_ecp_change_category_name_nonce': category_ecp_change_category_name_nonce
            },
            success: function (response) {

                if (response.data.error == 0) {
                    saveLoader.innerText = response.data.message;
                } else {
                    $('.ecp_category_select').each(function () {
                        $("option[value=" + category_id + "]").text(category_name);
                    });

                    $('.category-name').each(function () {
                        var name = $(this).text();

                        if (name == category_name_old) {
                            $(this).text(category_name);
                        }
                    });

                    _this.parents('.category_box').find('.ecp-category-name-show p').text(category_name);
                    $('.ecp-category-name-edit').addClass('ecp-hidden');
                    $('.ecp-category-name-show').removeClass('ecp-hidden');

                    saveLoader.classList.add('ecp-loading-success');
                    saveLoader.innerText = document.querySelector('label.saving-success').innerText;
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                saveLoader.innerText = document.querySelector('label.saving-error').innerText;
            }
        });
        setTimeout(function () {
            saveLoader.classList.remove('ecp-loading-success');
            saveLoader.classList.remove('show');
            saveLoader.innerText = document.querySelector('label.saving-default').innerText;
        }, 2000);
    });

    $(document).on('click', '.ecp-migrate-button', function (e) {
        e.preventDefault();
        var category_id = $(this).parents('.category_box').find('.category-id').val();
        var category_name = $(this).parents('.category_box').find('.ecp-category-name-show p').text();
        var category_name_default = $('.categories_list').find('.posts_number_default').parents('.category_box').find('.ecp-category-name-show p').text();

        $('.modal-category-name').text(category_name);

        $('.saving-category-name').text(category_name);

        $('#ex1').find('.ecp-move-posts-to-default').attr('data-id', category_id);
        $('#ex1').find('.ecp-move-posts-to-default').attr('data-name', category_name);
        $('#ex1').find('.ecp-move-posts-to-default').attr('data-namedefault', category_name_default);
    });

    $(document).on('click', '.ecp-delete-category-button', function (e) {
        e.preventDefault();
        var category_id = $(this).parents('.category_box').find('.category-id').val();
        var category_name = $(this).parents('.category_box').find('.ecp-category-name-show p').text();
        $('#delete-modal').find('.modal-category-name').text(category_name);
        $('#delete-modal').find('.ecp-delete-category').attr('data-id', category_id);
        $('.saving-category-name').text(category_name);
    });

    $(document).on('click', '.ecp-move-posts-to-default', function (e) {
        var _this = $(this);
        var category_id = $(this).attr('data-id');
        var category_name = $(this).attr('data-name');
        var category_ecp_move_posts_to_default_nonce = $(this).attr('data-ecp-move-posts-to-default-nonce');
        var category_name_default = $(this).attr('data-namedefault');

        // Call ajax
        const saveLoader = document.querySelector('.ecp-loading-saving');
        saveLoader.innerText = document.querySelector('label.saving-move-category').innerText;

        saveLoader.classList.add('show')

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxMoveToDefault',
                'category_id': category_id,
                'category_ecp_move_posts_to_default_nonce': category_ecp_move_posts_to_default_nonce,
            },
            success: function (response) {

                if (response.data.error == 0) {
                    saveLoader.innerText = response.data.message;
                } else {
                    var post_number = $('#category_box_' + category_id).find('.posts_number').text();
                    post_number = parseInt(post_number);
                    $('#category_box_' + category_id).find('.posts_number').text(0);

                    var post_number_default = $('.categories_list').find('.posts_number_default').text();
                    post_number_default = parseInt(post_number_default) + post_number;
                    $('.categories_list').find('.posts_number_default').text(post_number_default);

                    $('#ecp-post-list').find('span.category-name').each(function () {
                        if ($(this).text() == category_name) {
                            $(this).text(category_name_default);
                        }
                    });

                    $('.ecp_category_select').each(function () {
                        $("option[value=" + category_id + "]").remove();
                    });

                    $('#category_box_' + category_id).slideUp();

                    var category_default_id = $('.categories_list').find('.posts_number_default').parents('.category_box').find('.category-id').val();
                    $('#ecp-post-list').find('.saved-category-id').each(function () {
                        if ($(this).val() == category_id) {
                            $(this).val(category_default_id);
                        }
                    });


                    saveLoader.classList.add('ecp-loading-success');
                    saveLoader.innerText = document.querySelector('label.saved-move-category').innerText;
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                saveLoader.innerText = document.querySelector('label.saving-error').innerText;
            }
        });
        setTimeout(function () {
            saveLoader.classList.remove('ecp-loading-success');
            saveLoader.classList.remove('show');
            saveLoader.innerText = document.querySelector('label.saving-default').innerText;
        }, 2000);
    });

    $(document).on('click', '.ecp-delete-category', function (e) {
        var category_id = $(this).attr('data-id');
        var ecp_delete_category_nonce = $(this).attr('data-ecp-delete-category-nonce');

        // Call ajax
        const saveLoader = document.querySelector('.ecp-loading-saving');
        saveLoader.innerText = document.querySelector('label.saving-delete-category').innerText;

        saveLoader.classList.add('show')

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxDeleteCategory',
                'category_id': category_id,
                'ecp_delete_category_nonce': ecp_delete_category_nonce
            },
            success: function (response) {

                if (response.data.error == 0) {
                    saveLoader.innerText = response.data.message;
                } else {
                    $('.ecp_category_select').each(function () {
                        $("option[value=" + category_id + "]").remove();
                    });

                    $('#category_box_' + category_id).slideUp();

                    $('.saved-category-id').each(function () {
                        var category_saved_id = $(this).val();
                        if (category_saved_id == category_id) {
                            $(this).parents('.ecp-post-saved-template').slideUp();
                        }
                    });
                    saveLoader.classList.add('ecp-loading-success');
                    saveLoader.innerText = document.querySelector('label.saved-delete-category').innerText;
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                saveLoader.innerText = document.querySelector('label.saving-error').innerText;
            }
        });
        setTimeout(function () {
            saveLoader.classList.remove('ecp-loading-success');
            saveLoader.classList.remove('show');
            saveLoader.innerText = document.querySelector('label.saving-default').innerText;
        }, 2000);
    });

    $(document).on('click', '.ecp-change-name-button', function (e) {
        $(this).parents('.category_box').find('.ecp-category-name-show').addClass('ecp-hidden');
        $(this).parents('.category_box').find('.ecp-category-name-edit').removeClass('ecp-hidden');
    });

    $(document).on('click', '.change-category-name-cancel', function (e) {
        $(this).parents('.category_box').find('.ecp-category-name-show').removeClass('ecp-hidden');
        $(this).parents('.category_box').find('.ecp-category-name-edit').addClass('ecp-hidden');
    });

    $(document).on('click', '.ecp-add-schedule', function () {
        var day = $('#ecp_schedule_days').val();
        var hour = $('#ecp_schedule_hours').val();
        var minute = $('#ecp_schedule_minutes').val();
        var network = $('#ecp_schedule_network').val();
        var id = $(this).attr('data-id');
        var service = $(this).attr('data-service');
        var category_id = $('#ecp_schedule_categories').val();

        if (!network || category_id == '') {
            if (!network) {
                $('.ecp-show-networks').addClass('ecp-input-error');
            }
            if (category_id == '') {
                $('.ecp-show-category').addClass('ecp-input-error');
            }

            return false;
        } else {
            // handling of hours
            hour = Number(hour);

            const saveLoader = document.querySelector('.ecp-loading-saving');

            saveLoader.classList.add('show');

            // call ajax handle import into db
            $.ajax({
                url: ajaxurl,
                type: "post",
                data: {
                    'action': 'ajaxInsertData',
                    'day': day,
                    'hour': hour,
                    'minute': minute,
                    'network': network,
                    'id': id,
                    'service': service,
                    'category_id': category_id,
                    'ecp_profile': getUrlParameter('ecp_profile')
                },
                success: function (response) {
                    $('input[name=ecp-frequency]').each(function () {
                        $(this).prop('checked', false);
                    });

                    $('#ecp-post-freq-custom').prop('checked', true);
                    $('.add-schedule-block').removeClass('hidden');

                    $('.clear-schedule-box').removeClass('hidden');
                    $('#ecp-schedule-body').html(response.data);
                    saveLoader.classList.add('ecp-loading-success');
                    if (document.querySelector('label.saving-posting-time-success')) {
                        saveLoader.innerText = document.querySelector('label.saving-posting-time-success').innerText;
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    saveLoader.innerText = document.querySelector('label.saving-posting-time-error').innerText;
                }
            });

            setTimeout(function () {
                saveLoader.classList.remove('ecp-loading-success');
                saveLoader.classList.remove('show');
                saveLoader.innerText = document.querySelector('label.saving-default').innerText;
            }, 2000);
        }

    });

    function formatState(state) {
        if (!state.id) {
            return state.text;
        }
        var $state = $(
            '<span><img style="float: left" width="20" height="20" src="' + $(state.element).data('avatar') + '" class="img-flag" /> ' + state.text + '</span>'
        );
        return $state;
    };

    $(document).ready(function () {
        $(".js-example-templating").select2({
            templateResult: formatState
        });
    })

    $(document).on('mouseover', '.scheduled-item', function (e) {
        $(this).find('.ecp-remove-schedule').removeClass('hidden');
        $(this).find('.ecp-overlay span').addClass('hidden');
    });

    $(document).on('mouseleave', '.scheduled-item', function (e) {
        $(this).find('.ecp-remove-schedule').addClass('hidden');
        $(this).find('.ecp-overlay span').removeClass('hidden');
    });

    $(document).on('click', '.show-modal-delete', function () {
        var id = $(this).attr('data-id');
        var service = $(this).attr('data-service');
        var value = $(this).attr('data-value');
        var day = $(this).attr('data-day');
        var key = $(this).attr('data-key');
        var general = $(this).attr('data-general');

        $('#ecp-delete-schedule-modal').attr('data-id', id);
        $('#ecp-delete-schedule-modal').attr('data-service', service);
        $('#ecp-delete-schedule-modal').attr('data-value', value);
        $('#ecp-delete-schedule-modal').attr('data-day', day);
        $('#ecp-delete-schedule-modal').attr('data-key', key);
        $('#ecp-delete-schedule-modal').attr('data-general', general);
    });

    $(document).on('click', '.ecp-remove-schedule-action', function (e) {
        e.preventDefault();
        var id = $(this).attr('data-id');
        var service = $(this).attr('data-service');
        var value = $(this).attr('data-value');
        var day = $(this).attr('data-day');
        var key = $(this).attr('data-key');
        var general = $(this).attr('data-general');

        // call ajax handler update option
        const saveLoader = document.querySelector('.ecp-loading-saving');

        saveLoader.classList.add('show')

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxDeleteData',
                'value': value,
                'id': id,
                'day': day,
                'service': service,
                'key': key,
                'general': general,
                'ecp_profile': getUrlParameter('ecp_profile')
            },
            success: function (response) {
                $('input[name=ecp-frequency]').each(function () {
                    $(this).prop('checked', false);
                });

                $('#ecp-post-freq-custom').prop('checked', true);
                $('.add-schedule-block').removeClass('hidden');
                $('#ecp-schedule-body').html(response.data);

                saveLoader.classList.add('ecp-loading-success');
                saveLoader.innerText = document.querySelector('label.delete-posting-time-success').innerText;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                saveLoader.innerText = document.querySelector('label.delete-posting-time-error').innerText;
            }
        });

        setTimeout(function () {
            saveLoader.classList.remove('ecp-loading-success');
            saveLoader.classList.remove('show');
            saveLoader.innerText = document.querySelector('label.saving-default').innerText;
        }, 2000);
    });

    $(document).on('click', '.ecp-clear-schedule', function (e) {
        e.preventDefault();

        var id = $(this).attr('data-id');
        var service = $(this).attr('data-service');

        const saveLoader = document.querySelector('.ecp-loading-saving');

        saveLoader.classList.add('show')

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxClearData',
                'id': id,
                'service': service,
                'ecp_profile': getUrlParameter('ecp_profile')
            },
            success: function (response) {
                $('input[name=ecp-frequency]').each(function () {
                    $(this).prop('checked', false);
                });
                $('#ecp-post-freq-custom').prop('checked', true);
                $('.add-schedule-block').removeClass('hidden');

                $('.clear-schedule-box').addClass('hidden');
                $('#ecp-schedule-body').html(response.data);

                saveLoader.classList.add('ecp-loading-success');
                saveLoader.innerText = document.querySelector('label.clear-posting-time-success').innerText;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                saveLoader.innerText = document.querySelector('label.clear-posting-time-success').innerText;
            }
        });

        setTimeout(function () {
            saveLoader.classList.remove('ecp-loading-success');
            saveLoader.classList.remove('show');
            saveLoader.innerText = document.querySelector('label.saving-default').innerText;
        }, 2000);

    });

    $(document).on('click', '.post_buffer', function (e) {
        e.preventDefault();

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxPostBufferData',
                // 'service': service
            },
            success: function (response) {
                alert(response.data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('No matching posts found');
            }
        });

    });

    $('.ecp-network-single-setting').on('click', function () {
        const network = $(this);

        $('.ecp-show-networks').removeClass('ecp-input-error');
        var dataUsername = network.attr('data-username');
        var dataAvatar = network.attr('data-avatar');
        var dataSocial = network.attr('data-social');
        var dataProfile = network.attr('data-profile');

        if (dataProfile == 'all_networks' || dataProfile == '') {
            $('.ecp-show-networks').html(dataUsername + '</span></div></div><span class="ecp-grid-right dashicons dashicons-arrow-down-alt2"></span>');
            $('.ecp-show-networks').removeClass('pad0');
        } else {
            $('.ecp-show-networks').html('<div class="ecp-connected-profilex"> <div class="ecp-connected-profile-avatar"><img style="margin-right: 10px" class="ecp-profile-circle-medium" ng-src="' + dataAvatar + '" img-src-fallback="/wp-content/plugins/evergreen-content-poster/admin/img/ecp-profile-unknown.png" src="' + dataAvatar + '"><div class="ecp-social-network-icon-medium ' + dataSocial + '"></div><span class="ecp-connected-profile-network">' + dataUsername + '</span></div></div><span class="ecp-grid-right dashicons dashicons-arrow-down-alt2"></span>');
            $('.ecp-show-networks').addClass('pad0');
        }

        $('.ecp-network-list').removeClass('show');
        $('#ecp_schedule_network').val(dataProfile);
    });

    $('.ecp-network-single-setting').on('click', function () {
        const network = $(this);

        $('.ecp-show-networks').removeClass('ecp-input-error');
        var dataUsername = network.attr('data-username');
        var dataAvatar = network.attr('data-avatar');
        var dataSocial = network.attr('data-social');
        var dataProfile = network.attr('data-profile');

        if (dataProfile == 'all_networks' || dataProfile == '') {
            $('.ecp-show-networks').html(dataUsername + '</span></div></div><span class="ecp-grid-right dashicons dashicons-arrow-down-alt2"></span>');
            $('.ecp-show-networks').removeClass('pad0');
        } else {
            $('.ecp-show-networks').html('<div class="ecp-connected-profilex"> <div class="ecp-connected-profile-avatar"><img style="margin-right: 10px" class="ecp-profile-circle-medium" ng-src="' + dataAvatar + '" img-src-fallback="/wp-content/plugins/evergreen-content-poster/admin/img/ecp-profile-unknown.png" src="' + dataAvatar + '"><div class="ecp-social-network-icon-medium ' + dataSocial + '"></div><span class="ecp-connected-profile-network">' + dataUsername + '</span></div></div><span class="ecp-grid-right dashicons dashicons-arrow-down-alt2"></span>');
            $('.ecp-show-networks').addClass('pad0');
        }

        $('.ecp-network-list').removeClass('show');
        $('#ecp_schedule_network').val(dataProfile);
    });

    $('.ecp-day-single-setting').on('click', function () {
        const day = $(this);

        var dataDay = day.attr('data-day');
        var dataName = day.attr('data-name');

        $('.ecp-show-days').html(dataName + '</span></div></div><span class="ecp-grid-right dashicons dashicons-arrow-down-alt2 ecp-arrow-down"></span>');
        $('.ecp-show-days').removeClass('pad0');

        $('.ecp-day-list').removeClass('show');
        $('#ecp_schedule_days').val(dataDay);
    });

    $('.ecp-categories-single-setting').on('click', function () {
        const category = $(this);

        var dataId = category.attr('data-id');
        var dataName = category.attr('data-name');

        $('.ecp-show-category').html(dataName + '</span></div></div><span class="ecp-grid-right dashicons dashicons-arrow-down-alt2 ecp-arrow-down"></span>');
        $('.ecp-show-category').removeClass('pad0');

        $('.ecp-categories-list').removeClass('show');
        $('#ecp_schedule_categories').val(dataId);
    });

    $('.ecp-show-days').on('click', function () {
        $(this).next('.ecp-day-list').toggleClass('show');
    });

    $('.ecp-show-networks').on('click', function () {
        $(this).next('.ecp-network-list').toggleClass('show');
    });

    $('.ecp-show-category').on('click', function () {
        $(this).next('.ecp-categories-list').toggleClass('show');
    });

    $(document).on('change', '.post_per_day_change', function (e) {
        let radioTarget = $(this).data('radio');
        $('.' + radioTarget).trigger('click');

        let savedValField = document.querySelector('input[name="saved_posting_schedule"]');
        let newValField = document.querySelector('input[name="ecp_posting_schedule"]:checked');

        $('input[name="saved_posting_schedule"]').val(newValField.value);

        toggleSaveSetting(true);
    });

    $(document).on('click', '.ecp-dropdown-share', function (e) {
        e.preventDefault();
        $(this).parents('.ecp-dropdown').find('.ecp-my-dropdown').toggleClass("ecp-show");
    });

    // Close the dropdown menu if the user clicks outside of it
    window.onclick = function (event) {
        var buttons = document.getElementsByClassName('ecp-dropbtn');
        var i;
        var dropdowns = document.getElementsByClassName("ecp-dropdown-content");
        for (i = 0; i < buttons.length; i++) {
            if (!buttons[i].contains(event.target)) {
                dropdowns[i].classList.remove('ecp-show');
            }
        }
    }

    $(document).on('click', '.ecp-share-now-action', function (e) {
        e.preventDefault();

        var social_name = $(this).attr('data-social-name');
        var post_id = $(this).attr('data-id');
        var image = $(this).attr('data-image');
        var content = $(this).attr('data-content');

        const saveLoader = document.querySelector('.ecp-loading-saving');

        $('.social-network-name').text(social_name);
        saveLoader.innerText = document.querySelector('label.sharing-now').innerText;
        saveLoader.classList.add('show');

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxShareNow',
                'post_id': post_id,
                'image': image,
                //'link': link,
                'content': content,
                //'service': service
            },
            success: function (response) {
                if (response.data.error == 0) {
                    // saveLoader.html(response.data.message);
                    $('.ecp-loading-saving').html(response.data.message);
                    setTimeout(function () {
                        saveLoader.classList.remove('ecp-loading-success');
                        saveLoader.classList.remove('show');
                        saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                    }, 2000);
                } else {
                    saveLoader.classList.add('ecp-loading-success');
                    saveLoader.innerText = document.querySelector('label.shared-now').innerText;
                    setTimeout(function () {
                        saveLoader.classList.remove('ecp-loading-success');
                        saveLoader.classList.remove('show');
                        saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                    }, 2000);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                saveLoader.innerText = document.querySelector('label.default-error').innerText;
                setTimeout(function () {
                    saveLoader.classList.remove('ecp-loading-success');
                    saveLoader.classList.remove('show');
                    saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                }, 2000);
            }
        });
    });


    $(document).on('click', '.ecp-share-now-action-native', function (e) {
        e.preventDefault();

         // Add debugging
        console.log('Share button clicked');
        console.log('Data attributes:', {
            social_name: $(this).attr('data-social-name'),
            post_id: $(this).attr('data-id'),
            image: $(this).attr('data-image'),
            content: $(this).attr('data-content')
        });

        var social_name = $(this).attr('data-social-name');
        var post_id = $(this).attr('data-id');
        var image = $(this).attr('data-image');
        var content = $(this).attr('data-content');
        var ecp_share_now_action_native_nonce = $(this).attr('data-ecp-share-now-action-native-nonce');

        const saveLoader = document.querySelector('.ecp-loading-saving');

        $('.social-network-name').text(social_name);
        saveLoader.innerText = document.querySelector('label.sharing-now').innerText;
        saveLoader.classList.add('show');

        // Add debugging for AJAX data
        console.log('AJAX data being sent:', {
            action: 'ajaxShareNowNative',
            post_id: post_id,
            image: image,
            content: content,
            ecp_share_now_action_native_nonce: ecp_share_now_action_native_nonce
        });

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxShareNowNative',
                'post_id': post_id,
                'image': image,
                'content': content,
                'ecp_share_now_action_native_nonce': ecp_share_now_action_native_nonce
            },
            success: function (response) {
                console.log('AJAX Response:', response);
                if (response.data.error == 0) {
                    // saveLoader.html(response.data.message);
                    $('.ecp-loading-saving').html(response.data.message);
                    setTimeout(function () {
                        saveLoader.classList.remove('ecp-loading-success');
                        saveLoader.classList.remove('show');
                        saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                    }, 2000);
                } else {
                    saveLoader.classList.add('ecp-loading-success');
                    saveLoader.innerText = document.querySelector('label.shared-now').innerText;
                    setTimeout(function () {
                        saveLoader.classList.remove('ecp-loading-success');
                        saveLoader.classList.remove('show');
                        saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                    }, 2000);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                saveLoader.innerText = document.querySelector('label.default-error').innerText;
                setTimeout(function () {
                    saveLoader.classList.remove('ecp-loading-success');
                    saveLoader.classList.remove('show');
                    saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                }, 2000);
            }
        });
    });

    $(document).on('click', '.ecp-add-queue-action', function (e) {
        e.preventDefault();

        var social_name = $(this).attr('data-social-name');
        //var profile_id = $(this).attr('data-profile');
        var post_id = $(this).attr('data-id');
        var image = $(this).attr('data-image');
        //var link = $(this).attr('data-link');
        var content = $(this).attr('data-content');
        //var service = $(this).attr('data-service');

        const saveLoader = document.querySelector('.ecp-loading-saving');

        $('.social-network-name').text(social_name);
        saveLoader.innerText = document.querySelector('label.adding-now').innerText;
        saveLoader.classList.add('show');

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ajaxAddToQueue',
                'post_id': post_id,
                //'profile_id': profile_id,
                'image': image,
                //'link': link,
                'content': content,
                //'service': service
            },
            success: function (response) {
                if (response.data.error == 0) {
                    $('.ecp-loading-saving').html(response.data.message);
                    setTimeout(function () {
                        saveLoader.classList.remove('ecp-loading-success');
                        saveLoader.classList.remove('show');
                        saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                    }, 2000);
                } else {
                    saveLoader.classList.add('ecp-loading-success');
                    saveLoader.innerText = document.querySelector('label.added-now').innerText;
                    setTimeout(function () {
                        saveLoader.classList.remove('ecp-loading-success');
                        saveLoader.classList.remove('show');
                        saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                    }, 2000);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                saveLoader.innerText = document.querySelector('label.default-error').innerText;
                setTimeout(function () {
                    saveLoader.classList.remove('ecp-loading-success');
                    saveLoader.classList.remove('show');
                    saveLoader.innerText = document.querySelector('label.saving-default').innerText;
                }, 2000);
            }
        });
    });

    $(document).on('click', '.scroll_to_ecp_box', function (e) {
        e.preventDefault();
        $('html, body').animate({
            scrollTop: eval($("#evergreen-fields").offset().top - 50)
        }, 1000);
    });

    $(document).on('click', '.ecp-next', function (e) {
        e.preventDefault();
        if ($(this).attr('disabled') != 'disabled') {
            var step = $(this).attr('data-step');
            $.ajax({
                url: ajaxurl,
                type: "post",
                data: {
                    'action': 'ajaxStepCompleted',
                    'step': step
                },
                success: function (response) {
                    if (step == 5) {
                        // Process step 5
                        var type_business = '';
                        var type_entrepreneur = '';
                        var type_business_value = '';
                        var type_entrepreneur_value = '';
                        $('.type-business .ecp-content-border').each(function (e) {
                            if ($(this).hasClass('ecp-selected')) {
                                type_business = $(this).attr('data-type');
                                type_business_value = $(this).attr('data-value');
                            }
                        });

                        $('.ecp-entrepreneur .ecp-content-border').each(function (e) {
                            if ($(this).hasClass('ecp-selected')) {
                                type_entrepreneur = $(this).attr('data-entrepreneur');
                                type_entrepreneur_value = $(this).attr('data-value');
                            }
                        });
                        var industry = $('#ecp-configurator-industry').val();

                        $.ajax({
                            url: ajaxurl,
                            type: "post",
                            data: {
                                'action': 'ajaxStep5Save',
                                'type_business': type_business,
                                'type_entrepreneur': type_entrepreneur,
                                'industry': industry,
                                'type_business_value': type_business_value,
                                'type_entrepreneur_value': type_entrepreneur_value
                            },
                            success: function (response) {
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.log('Error');
                            }
                        });
                    }

                    $('.ecp-item').each(function () {
                        if ($(this).hasClass('active')) {
                            var id = $(this).attr('id');
                            if (step < 7) {
                                var step_next = parseInt(step) + 1;
                            } else {
                                var step_next = 1;
                            }
                            $(this).removeClass('active');
                            $(this).addClass('completed');
                            $(this).find('text').html('&check;');
                            $('#navstep-' + step_next).addClass('active');
                            location.hash = '';
                            var url = window.location.href;
                            if (url.slice(-1) == '#') {
                                url = url.substring(0, url.length - 1);
                            }
                            var replace_url = url + '#step-' + step_next;
                            window.history.pushState('', '', replace_url);
                            return false;
                        }
                    });
                    $('.step-content').each(function () {
                        if ($(this).hasClass('active')) {
                            var id = $(this).attr('id');
                            if (step < 7) {
                                var step_next = parseInt(step) + 1;
                            } else {
                                var step_next = 1;
                            }
                            $(this).removeClass('active');
                            $('#step-' + step_next).addClass('active');
                            return false;
                        }
                    });
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error');
                }
            });
        }
    });
    $(document).on('click', '.ecp-previous', function (e) {
        e.preventDefault();
        var step = $(this).attr('data-step');
        $('.ecp-item').each(function () {
            if ($(this).hasClass('active')) {
                var id = $(this).attr('id');
                if (step == 1) {
                    return false;
                } else {
                    var step_next = parseInt(step) - 1;
                }
                $(this).removeClass('active');
                $('#navstep-' + step_next).addClass('active');
                location.hash = '';
                var url = window.location.href;
                if (url.slice(-1) == '#') {
                    url = url.substring(0, url.length - 1);
                }
                var replace_url = url + '#step-' + step_next;
                window.history.pushState('', '', replace_url);
                return false;
            }
        });
        $('.step-content').each(function () {
            if ($(this).hasClass('active')) {
                var id = $(this).attr('id');
                if (step == 1) {
                    return false;
                } else {
                    var step_next = parseInt(step) - 1;
                }
                $(this).removeClass('active');
                $('#step-' + step_next).addClass('active');
                return false;
            }
        });
    });

    var hash = window.location.hash.substr(1);
    if (hash) {
        var step = hash.slice(-1);
        $('.step-content').each(function () {
            $(this).removeClass('active');
        });
        $('.ecp-item').each(function () {
            $(this).removeClass('active');
        });
        $('#step-' + step).addClass('active');
        $('#navstep-' + step).addClass('active');
    } else {
        $('#step-1').addClass('active');
        $('#navstep-1').addClass('active');
    }

    $(document).on('click', '.type-business .ecp-content-border', function (e) {
        e.preventDefault();
        var _this = $(this);
        var type_business = $(this).attr('data-type');
        $('.type-business .ecp-content-border').each(function () {
            $(this).removeClass('ecp-selected');
        });
        _this.addClass('ecp-selected');
        $('.entrepreneur').removeClass('hidden');
        $('.ecp-entrepreneur').each(function () {
            $(this).addClass('hidden');
        });
        $('.' + type_business).removeClass('hidden');
        $('.entrepreneur p strong').text($('.label-' + type_business).text());
    });

    $(document).on('click', '.ecp-entrepreneur .ecp-content-border', function (e) {
        e.preventDefault();
        var _this = $(this);
        $('.ecp-entrepreneur .ecp-content-border').each(function () {
            $(this).removeClass('ecp-selected');
        });
        _this.addClass('ecp-selected');

        $('.industry').removeClass('hidden');
    });

    $(document).on('click', '#settings_subscribe', function (e) {
        e.preventDefault();
        $('.email-error').addClass('hidden');
        $('.email-success').addClass('hidden');

        var email = $('#ecp-newsletter-subcribe').val();

        var constraints = {
            from: {
                email: true
            }
        };

        var check = validate({ from: email }, constraints);
        if (typeof check !== "undefined") {
            $('.email-error').removeClass('hidden');
        } else {
            // Save to DB api
            $.ajax({
                url: ajaxurl,
                type: "post",
                data: {
                    'action': 'ajaxSaveContact',
                    'email': email
                },
                success: function (response) {
                    $('.email-success').removeClass('hidden');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('.email-error').text('An error occurred, please try again');
                    $('.email-error').removeClass('hidden');
                }
            });
        }
    });

    $(document).on('click', '#deactivate-evergreen-content-poster', function (e) {
        e.preventDefault();
        var _this = $(this);
        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'ecp_deactivation_popup',
            },
            success: function (response) {
                $('body').append(response.html);
                $('.ecp-modal--additional-link').attr('href', _this.attr('href'));
                $('.ecp-submit-deactive').attr('data-url', _this.attr('href'));
                $('.ecp-submit-deactive').attr('data-current-url', window.location.href);
            },
            error: function (jqXHR, textStatus, errorThrown) {

            }
        });
    });

    $(document).on('change', 'input[type=radio][name=ecp-survey-radios]', function (e) {
        var _this = $(this);
        $('.ecp-survey-extra-field').each(function () {
            $(this).hide();
            $(this).find('textarea').prop('disabled', 'disabled');
            $(this).find('input').prop('disabled', 'disabled');
        });
        _this.parents('.ecp-option').find('.ecp-survey-extra-field').show();
        _this.parents('.ecp-option').find('.ecp-survey-extra-field textarea').prop('disabled', false);
        _this.parents('.ecp-option').find('.ecp-survey-extra-field input').prop('disabled', false);
    });

    $(document).on('click', '.ecp-cancel-deactive', function (e) {
        e.preventDefault();
        $('.ecp-deactivate-popup').remove();
    });

    $(document).on('click', '.ecp-modal--additional-link', function () {
        $('.ecp-modal-controls .spinner').show();
    });

    $(document).on('click', '.ecp-submit-deactive', function (e) {
        e.preventDefault();

        $('.ecp-modal-controls .spinner').show();
        var _this = $(this);
        _this.attr('disabled', true);
        var url = _this.attr('data-url');
        var current_url = _this.attr('data-current-url');
        var delete_all_data = 0;
        if ($('input[name=ecp-confirm_reset_store]').is(':checked')) {
            delete_all_data = 1;
        }
        var value = '';
        $('input[name=ecp-survey-radios]').each(function () {
            if ($(this).is(':checked')) {
                value = $(this).val();
            }
        });

        var reason = '';
        $('[name=ecp-user-reason]').each(function () {
            if ($(this).attr('disabled') !== 'disabled' || !$(this).is(':disabled')) {
                reason = $(this).val();
            }
        });

        // $.ajax({
        //     url: ajaxurl,
        //     type: "post",
        //     data: {
        //         'action': 'ecp_deactivation_submit',
        //         'url_ri': url,
        //         'value': value,
        //         'reason': reason,
        //         'delete_all_data': delete_all_data
        //     },
        //     success: function (response) {
        //         window.location.href = url;
        //     },
        //     error: function(jqXHR, textStatus, errorThrown) {
        //         window.location.href = url;
        //     }
        // });

        const data = {
            'action': 'ecp_deactivation_submit',
            'url_ri': url,
            'current_url': current_url,
            'value': value,
            'reason': reason,
            'delete_all_data': delete_all_data
        };
        jQuery.post(
            ajaxSettings.ajaxurl,
            data,
            function (response) {
                if (response.success) {
                    window.location.href = url;
                } else {
                    window.location.href = url;
                }

            });
    });

    $(document).on('click', '.ecp-container__configuration-wizard--dismiss', function (e) {
        e.preventDefault();
        var _this = $(this);
        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'updateDismissConfig',
            },
            success: function (response) {
                $('.top-notify').addClass('hidden');
                $('.top-notify-dismiss').removeClass('hidden');
                _this.addClass('hidden');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log('Error');
            }
        });
    });

    $(document).on('click', '.ecp-container__configuration-wizard--dismiss--cron', function (e) {
        e.preventDefault();
        var _this = $(this);
        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'updateDismissCronConfig',
            },
            success: function (response) {
                $('.ecp-error-container-notification').addClass('ecp-hidden');
                _this.parents('.notice-error').hide();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log('Error');
            }
        });
    });

    $(document).on('click', '.ecp-container__configuration-wizard--dismiss--translate', function (e) {
        e.preventDefault();
        var _this = $(this);
        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'updateDismissTranslateConfig',
            },
            success: function (response) {
                $('.ecp-container-translate-notification').addClass('ecp-hidden');
                _this.parents('.notice-error').hide();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log('Error');
            }
        });
    });

    $(document).on('click', '.ecp-show-modal-popup', function (e) {
        var id = $(this).attr('href');
        var _this = $(this);
        $('#ecp-profile-posts-block').each(function (e) {
            $(this).remove();
        });
        $(id + ' .add-new-ecp-post').append('<div id="ecp-profile-posts-block"></div>');
        $('#ecp-post-ref').val(_this.attr('data-id'));

        $('.ecp-saved-posts-block-popup').each(function () {
            $(this).attr('id', '');

        });
        $('.ecp-loading-saving-popup').each(function () {
            $(this).removeClass('ecp-loading-saving');

        });

        $(id).find('.ecp-saved-posts-block-popup').attr('id', 'ecp-saved-posts-block');
        $(id).find('.ecp-loading-saving-popup').addClass('ecp-loading-saving');
    });

    $(document).on('click', '.ecp-category-edit-button', function (e) {
        var _this = $(this);
        var cate_id = _this.attr('data-id');
        var random_availability = _this.attr('data-random');
        var cate_title = $('#cat-' + cate_id).find('.ecp-category-title').html();
        cate_title = cate_title.trim();
        var cate_des = $('#cat-' + cate_id).find('.ecp-category-description').html();
        cate_des = cate_des.trim();

        $('#edit-category').find('.category-name-input').val(cate_title);
        $('#edit-category').find('.category-description-input').val(cate_des);

        if (random_availability == 1) {
            $('#edit-category').find('.random_availability').attr('checked', 'checked');
            $('#edit-category').find('.saved_random_availability').val('on');
            $('#edit-category').find('.ecp-checkbox-toggle-small').attr('data-status', 'on');
        } else {
            $('#edit-category').find('.saved_random_availability').val('off');
            $('#edit-category').find('.ecp-checkbox-toggle-small').attr('data-status', 'off');
        }

        $('.ecp-edit-category-button').attr('data-id', cate_id);
    });

    $(document).on('click', '#edit-category .ecp-checkbox-toggle-small', function () {

        var toggler = $(this);

        var status = toggler.attr('data-status') == 'off' ? 'on' : 'off';

        toggler.attr('data-status', status);

        var category_id = toggler.parents('.category_box').find('.category-id').val();
        toggler.find('.change_status').removeAttr('style');

    });

    $(document).on('click', '.add-new-category-step', function (e) {
        $('#add-category').find('.ecp-admin-input').val('');
        $('#add-category').find('.ecp-checkbox-toggle-small').attr('data-status', 'on');
    });

    $(document).on('click', '#add-category .ecp-checkbox-toggle-small', function () {

        var toggler = $(this);

        var status = toggler.attr('data-status') == 'off' ? 'on' : 'off';

        toggler.attr('data-status', status);

        var category_id = toggler.parents('.category_box').find('.category-id').val();
        toggler.find('.change_status').removeAttr('style');

    });

    $(document).on('click', '.ecp-edit-category-button', function (e) {
        e.preventDefault();
        $('.edit-category-action .spinner').show();
        var _this = $(this);
        var cate_name = $('#edit-category .category-name-input').val();
        var cate_des = $('#edit-category .category-description-input').val();
        var random_availability = $('#edit-category .ecp-checkbox-toggle-small').attr('data-status');
        if (random_availability == 'on') {
            random_availability = 1;
        } else {
            random_availability = 0;
        }
        var cate_id = _this.attr('data-id');

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'updateCategoryStep',
                'cate_id': cate_id,
                'cate_name': cate_name,
                'cate_des': cate_des,
                'random_availability': random_availability
            },
            success: function (response) {
                $('#cat-' + cate_id).find('.ecp-category-title').html(cate_name);
                $('#cat-' + cate_id).find('.ecp-category-description').html(cate_des);
                $('#cat-' + cate_id).find('.ecp-category-edit-button').attr('data-random', random_availability);
                $('.edit-category-action .spinner').hide();
                // $('#edit-category').hide();

                $('.ecp_category_select').each(function () {
                    $("option[value=" + cate_id + "]").text(cate_name);
                });

                $('.ecp-category-library').each(function () {
                    $("option[value=" + cate_id + "]").text(cate_name);
                });

                $('.ecp-category-scheduler').each(function () {
                    $("option[value=" + cate_id + "]").text(cate_name);
                });

                $('.jquery-modal').hide();
                $('body.admin_page_evergreen-content-configurator').removeAttr('style');

                //update
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log('Error');
            }
        });
    });

    $(document).on('click', '.ecp-add-category-button', function (e) {
        e.preventDefault();
        $('.edit-category-action .spinner').show();
        var _this = $(this);
        var cate_name = $('#add-category .category-name-input').val();
        var cate_des = $('#add-category .category-description-input').val();
        var random_availability = $('#add-category .ecp-checkbox-toggle-small').attr('data-status');
        if (random_availability == 'on') {
            random_availability = 1;
        } else {
            random_availability = 0;
        }

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'addCategoryStep',
                'cate_name': cate_name,
                'cate_des': cate_des,
                'random_availability': random_availability
            },
            success: function (response) {
                $('.edit-category-action .spinner').hide();

                const savedCategoryStep = $('.ecp-category-template.ecp-hidden').clone();
                savedCategoryStep.attr('id', 'cat-' + response.data);
                savedCategoryStep.find('.ecp-category-title').text(cate_name);
                savedCategoryStep.find('.ecp-category-description').text(cate_des);
                savedCategoryStep.find('.ecp-category-edit-button').attr('data-random', random_availability);
                savedCategoryStep.find('.ecp-category-edit-button').attr('data-id', response.data);
                savedCategoryStep.find('.ecp-delete-button').attr('data-id', response.data);
                savedCategoryStep.removeClass('ecp-hidden');

                savedCategoryStep.appendTo('.categories-box');

                $('.ecp-category-scheduler').append('<option value="' + response.data + '">' + cate_name + '</option>');
                $('.ecp_category_select').each(function () {
                    $(this).append('<option value="' + response.data + '">' + cate_name + '</option>');
                });

                $('.ecp-category-library').each(function () {
                    $(this).append('<option value="' + response.data + '">' + cate_name + '</option>');
                });

                $('.jquery-modal').hide();
                $('body.admin_page_evergreen-content-configurator').removeAttr('style');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log('Error');
            }
        });
    });

    $(document).on('click', '.ecp-delete-button', function (e) {
        e.preventDefault();

        var cate_id = $(this).attr('data-id');
        var cate_name = $('#cat-' + cate_id).find('.ecp-category-title').text();

        $('#delete-category').find('.modal-category-name').text(cate_name);
        $('#delete-category').find('.ecp-delete-category-step').attr('data-id', cate_id);
    });

    $(document).on('click', '.ecp-delete-category-step', function (e) {
        e.preventDefault();

        $('.edit-category-action .spinner').show();
        var cate_id = $(this).attr('data-id');

        $.ajax({
            url: ajaxurl,
            type: "post",
            data: {
                'action': 'deleteCategoryStep',
                'cate_id': cate_id
            },
            success: function (response) {
                $('.edit-category-action .spinner').hide();
                $('#cat-' + cate_id).remove();

                $('.ecp_category_select').each(function () {
                    $("option[value=" + cate_id + "]").remove();
                });

                $('.ecp-category-library').each(function () {
                    $("option[value=" + cate_id + "]").remove();
                });

                $('.ecp-category-scheduler').each(function () {
                    $("option[value=" + cate_id + "]").remove();
                });

                $('.saved-category-id').each(function () {
                    var category_saved_id = $(this).val();
                    if (category_saved_id == cate_id) {
                        $(this).parents('.ecp-post-saved-template').slideUp();
                    }
                });

                $('.jquery-modal').hide();
                $('body.admin_page_evergreen-content-configurator').removeAttr('style');
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log('Error');
            }
        });
    });

    $(document).on('click', '.close-modal-class', function (e) {
        e.preventDefault();
        $(this).closest('.modal').toggle('hide');
    });

    $(document).on('click', '.ecp-schedule-custom-select-yes', function (e) {
        $('#use-pre-schedule').val(1);
        $("input[name=ecp-frequency]:last").prop("checked", true).trigger("click");
        $("input[name=ecp-frequency]:last").change();
        $('.show-popup-confirm-custom').attr('href', '');
        $('.show-popup-confirm-custom').attr('rel', '');
        $.modal.close();
    });

    $(document).on('click', '.ecp-schedule-custom-select-no', function (e) {
        $('#use-pre-schedule').val(0);
        $("input[name=ecp-frequency]:last").prop("checked", true).trigger("click");
        $("input[name=ecp-frequency]:last").change();
        $('.show-popup-confirm-custom').attr('href', '');
        $('.show-popup-confirm-custom').attr('rel', '');
        $.modal.close();
    });

    $(document).on('change', 'input[name=ecp-frequency]', function (e) {
        e.preventDefault();
        var frequency = $(this).val();
        var id = $(this).attr('data-id');
        var service = $(this).attr('data-service');
        var use_pre_schedule = $('#use-pre-schedule').val();

        var isset_custom = $('#isset_custom_frequency').val();

        if (frequency == 'custom' && isset_custom == 0) {
            $.ajax({
                url: ajaxurl,
                type: "post",
                data: {
                    'action': 'genScheduleByFrequency',
                    'frequency': frequency,
                    'id': id,
                    'service': service,
                    'ecp_profile': getUrlParameter('ecp_profile'),
                    'use_pre_schedule': use_pre_schedule
                },
                success: function (response) {
                    $('#ecp-schedule-body').html(response.data);
                    $('.clear-schedule-box').removeClass('hidden');
                    if (frequency == 'custom') {
                        $('.add-schedule-block').removeClass('hidden');
                    } else {
                        $('.add-schedule-block').addClass('hidden');
                    }

                    $('#isset_custom_frequency').val(1);

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error');
                }
            });
        } else {
            $.ajax({
                url: ajaxurl,
                type: "post",
                data: {
                    'action': 'genScheduleByFrequency',
                    'frequency': frequency,
                    'id': id,
                    'service': service,
                    'ecp_profile': getUrlParameter('ecp_profile')
                },
                success: function (response) {
                    $('#ecp-schedule-body').html(response.data);
                    $('.clear-schedule-box').removeClass('hidden');
                    if (frequency == 'custom') {
                        $('.add-schedule-block').removeClass('hidden');
                    } else {
                        $('.add-schedule-block').addClass('hidden');
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error');
                }
            });
        }
    });

    var windowsize = $(window).width();
    if (windowsize <= 1440) {
        //if the window is greater than 440px wide then turn on jScrollPane..
        $('.ecp-configurator-container').removeClass('ecp-col-540').addClass('ecp-col-700');
    }

    var border_width = $('.ecp-wrapper .ecp-section .ecp-configurator-content').width();
    $('.abc .ecp-box-border').width(border_width);
    $(window).on('resize', function () {
        var border_width = $('.ecp-wrapper .ecp-section .ecp-configurator-content').width();
        $('.abc .ecp-box-border').width(border_width);
    });

    $(document).on('click', '.ecp-delete-message', function (e) {
        setTimeout(function () {
            $('.close-modal').removeAttr('rel').addClass('jquery-modal-close');
        }, 1000);

    });


});


/* global ajaxurl, ecpAdminOptionsData, wp */
(function (window, $) {
    'use strict';

    window.onload = function () {

        /*********************************************************
         A Function send the array of setting to ajax.php
         *********************************************************/
        function selectText(element) {
            var range, selection;

            if (document.body.createTextRange) {
                range = document.body.createTextRange();
                range.moveToElementText(element);
                range.select();

            } else if (window.getSelection) {
                selection = window.getSelection();
                range = document.createRange();

                range.selectNodeContents(element);
                selection.removeAllRanges();
                selection.addRange(range);

            }
        }

        /*********************************************************
         Checkboxes
         *********************************************************/

        function populateOptions() {
            jQuery('form.ecp-admin-settings-form input, form.ecp-admin-settings-form select').on('change', function () {
                toggleSaveSetting();
            });

        }

        /*********************************************************
         A Function to change the color of the save button
         *********************************************************/
        function saveColorToggle() {

        }

        /*********************************************************
         A Function to update the button sizing options
         *********************************************************/
        function updateScale() {
            jQuery('select[name="button_size"],select[name="button_alignment"]').on('change', function () {
                jQuery('.ecp_social_panel').css({ width: '100%' });

                var width = jQuery('.ecp_social_panel').width();
                var scale = jQuery('select[name="button_size"]').val();
                var align = jQuery('select[name="button_alignment"]').val();
                var newWidth;

                if ((align == 'full_width' && scale != 1) || scale >= 1) {
                    newWidth = width / scale;

                    jQuery('.ecp_social_panel').css('cssText', 'width:' + newWidth + 'px!important;');

                    jQuery('.ecp_social_panel').css({
                        transform: 'scale(' + scale + ')',
                        'transform-origin': 'left'
                    });
                } else if (align != 'full_width' && scale < 1) {
                    newWidth = width / scale;

                    jQuery('.ecp_social_panel').css({
                        transform: 'scale(' + scale + ')',
                        'transform-origin': align
                    });
                }

            });
        }

        /*******************************************************
         Make the buttons sortable
         *******************************************************/

        jQuery(document).ready(function () {
            populateOptions();
            updateScale();
            getSystemStatus();
            getSystemStatusLogFile();
        });

        /*******************************************************
         Expand the system status debugging information
         *******************************************************/
        function getSystemStatus() {
            jQuery('.ecp-system-status').on('click', function (event) {
                event.preventDefault();

                jQuery('.ecp-system-status-wrapper').slideToggle();

                selectText(jQuery('.ecp-system-status-container').get(0));
            });
        }

        /*******************************************************
         Expand the status logfile debugging information
         *******************************************************/
        function getSystemStatusLogFile() {
            jQuery('.ecp-status-logfile').on('click', function (event) {
                event.preventDefault();

                jQuery('.ecp-system-status--logfile-wrapper').slideToggle();

                selectText(jQuery('.ecp-system-status--logfile-container').get(0));
            });
        }
    };

})(this, jQuery);
